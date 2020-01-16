package easyconfig

import easyconfig.DefaultReader.opt2Either.at
import easyconfig.EasyConfig.folder.at
import easyconfig.Override.Aux
import shapeless._
import shapeless.ops.hlist._

object Test3 extends App {

  case class Foo(num: Int, str: String = "default")

  val defaults = DefaultReader[Foo]
  println("Defaults: " + defaults.readDefault)

  val envReader = EnvReader[Foo]
  println("Env: " + envReader.readEnv)

  val argReader = ArgReader[Foo]
  val myArgs = List("--num", "42", "--str", "hello")
  println("Args: " + argReader.readArgs(myArgs))

  val orEnv = Override[defaults.Out, envReader.Out]
  val overriddenByEnv = orEnv.overrideLeft(defaults.readDefault, envReader.readEnv)
  println("Defaults overriden by env: " + overriddenByEnv)

  val orArg = Override[defaults.Out, argReader.Out]
  val overriddenByArgs = orArg.overrideLeft(defaults.readDefault, argReader.readArgs(myArgs))
  println("Defaults overriden by args: " + overriddenByArgs)


//  val config = EasyConfig[Foo]
//  println("esayConfig: " + config.getConfig(myArgs))

  object folder5 extends Poly2{
    implicit def allCase[E <: AllError, A, ACC <: HList, O <: HList]: Case.Aux[Either[E, A], Either[E, ACC], Either[E, A :: ACC]] =
      at { (a, acc) =>
        acc match {
          case Left(e) => Left(e)
          case Right(hl) => a match {
            case Right(value) => Right(value :: hl)
            case Left(e) => Left(e)
          }
        }

      }
  }

  val hlist2 = (Right("str"): Either[AllError, String]) :: (Left(DefaultNotFound("?")): Either[AllError, Int]) :: (Right(1): Either[AllError, Int]) :: HNil

  val o = hlist2.foldRight(Right(HNil): Either[AllError, HNil])(folder5)
  println(o)

  val easyConfig = EasyConfig[Foo]
  val easyConfigResult = easyConfig.getConfig(myArgs)
  println("easyConfigResult: " + easyConfigResult)

  val gen = Generic[Foo]
  println("Test: " + easyConfigResult.map(hl => gen.from(hl)))

  println("Done: " + Done[Foo].getConfig(myArgs))
}
