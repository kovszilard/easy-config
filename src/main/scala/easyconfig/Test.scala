package easyconfig

import EnvReader._
import shapeless._
import shapeless.ops._
import shapeless.labelled.FieldType

object Test extends App {

  case class Foo(str: String, num: Int = 100)

  val fromEnv = EnvReader[Foo].readEnv

  val default = Default.AsOptions[Foo].apply()

//  println(default.apply())

  val zipped = fromEnv.zip(default)

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

//  def readConfig[C, CO <: HList, DO <: HList, EO <: HList](implicit gen: Generic.Aux[C, CO], envReader: EnvReader.Aux[CO, EO], default: Default.AsOptions.Aux[C, DO]): C = {
//    val fromEnv = envReader.readEnv
//    val default0 = default.apply()
//    val zipped = fromEnv.zip(default0)
//    val finalEnv = zipped.map(envOrDefault)
//    val finalHlist = finalEnv.map(envOrThrow)
//    gen.from(finalHlist)
//  }


}
