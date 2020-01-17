package easyconfig

import easyconfig.DefaultReader.opt2Either.at
import easyconfig.CompoundReader.folder.at
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

  val compoundReader = CompoundReader[Foo]
  val compoundReaderResult = compoundReader.getConfig(myArgs)
  println("compoundReaderResult: " + compoundReader)

  println("ConfigRequest: " + ConfigRequest[Foo].getConfig(myArgs))

  println("Nice: " + easyConfig[Foo](List.empty))
  println("Nice2: " + easyConfig[Foo](myArgs))

  println("Default.AsRecord: " + Default.AsRecord[Foo].apply())
}
