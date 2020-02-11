package easyconfig

import easyconfig.readers.DefaultReader
import shapeless._

object Test4 extends App {

  case class Foo(num: Int, helloWorld: String = "default")

  println(easyConfig[Foo](List("--num", "2", "--help")))

//  val defaults = DefaultReader[Foo]
//  println(defaults.readDefault)
}
