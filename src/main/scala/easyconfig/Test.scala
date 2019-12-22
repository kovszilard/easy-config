package easyconfig

import EnvReader._
import shapeless._
import shapeless.ops._
import shapeless.syntax._
import shapeless.labelled.FieldType

object Test extends App {

  case class Foo(str: String, num: Int = 100)

  val fromEnv = EnvReader[Foo].readEnv

  val default = Default.AsOptions[Foo].apply()

//  println(default.apply())

  val zipped = fromEnv.zip(default)

  println(zipped)

  object envOrDefault extends Poly1 {
    implicit def allCase[A]: Case.Aux[(Either[AllErrors, A], Option[A]), Either[AllErrors, A]] =
      at{
        case (Right(x), _) => Right(x)
        case (Left(_), Some(y)) => Right(y)
        case (Left(err), None) => Left(err)
      }
  }

  val finalEnv = zipped.map(envOrDefault)

  object envOrThrow extends Poly1 {
    implicit def allCase[A]: Case.Aux[Either[AllErrors, A], A] = at{
      case Right(x) => x
      case _ => throw new RuntimeException("ohh noooo!")
    }
  }

  println(Generic[Foo].from(finalEnv.map(envOrThrow)))

//  def readConfig[H1 <: HList, H1O <: HList](implicit envReader: EnvReader.Aux[H1, H1O]): HList = {
//    val fromEnv = envReader.readEnv
//    fromEnv.zip(fromEnv)
////    val zipped = fromEnv.zip(default0)
////    val finalEnv = zipped.map(envOrDefault)
////    val finalHlist = finalEnv.map(envOrThrow)
////    gen.from(finalHlist)
//  }


}
