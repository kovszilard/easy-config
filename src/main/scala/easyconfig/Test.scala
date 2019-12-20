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
    implicit def aCase[A]: Case.Aux[(Either[AllErrors, A], Option[A]), Either[AllErrors, A]] =
      at{
        case (Right(x), _) => Right(x)
        case (Left(_), Some(y)) => Right(y)
        case (Left(err), None) => Left(err)
      }
  }

  val finalEnv = zipped.map(envOrDefault)

  println(finalEnv)
}
