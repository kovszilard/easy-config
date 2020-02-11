package easyconfig

import readers._
import helpers._

object Test3 extends App {

  case class Foo(num: Int, str: String = "default")

  val defaults = DefaultReader[Foo]
  println("Defaults: " + defaults.readDefault)

  val envReader = EnvReader[Foo]
  println("Env: " + envReader.readEnv)

  val o1 = Override[defaults.Out, envReader.Out]
  println("o1: " + o1.overrideLeft(defaults.readDefault, envReader.readEnv))

  val argReader = ArgReader[Foo]
  val myArgs = List("--num", "42", "--str", "hello")
  println("Args: " + argReader.readArgs(myArgs))

  val o2 = Override[o1.Out, argReader.Out]
  println("o2: " + o2.overrideLeft(o1.overrideLeft(defaults.readDefault, envReader.readEnv), argReader.readArgs(List.empty)))

  val orEnv = Override[defaults.Out, envReader.Out]
  val overriddenByEnv = orEnv.overrideLeft(defaults.readDefault, envReader.readEnv)
  println("Defaults overriden by env: " + overriddenByEnv)

  val orArg = Override[defaults.Out, argReader.Out]
  val overriddenByArgs = orArg.overrideLeft(defaults.readDefault, argReader.readArgs(myArgs))
  println("Defaults overriden by args: " + overriddenByArgs)

  val compoundReader = CompoundReader[Foo]
  val compoundReaderResult = compoundReader.getConfig(myArgs)
  println("compoundReaderResult: " + compoundReader)

  println("GenConfig: " + GenConfig[Foo].getConfig(myArgs))

  println("Nice2: " + easyConfig[Foo](myArgs))
  println("Nice: " + easyConfig[Foo](List.empty))

}
