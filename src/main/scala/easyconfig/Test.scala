package easyconfig

import Config._

case class Foo(num: Int)
case class Bar(num: Int, str: String)

object Test extends App {

  println("Foo from env: " + readFromEnv[Foo])

  println("Bar from env: " + readFromEnv[Bar])

}
