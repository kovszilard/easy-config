package easyconfig

import java.util.Date

import scala.concurrent.duration.Duration

object Test4 extends App {

  case class Foo(num: Duration, helloWorld: String = "default")

  println(easyConfig[Foo](List("--num", "1 s")))
  println(easyConfig[Foo](List("--num", "1 s", "--hello-world", "X")))
  println(easyConfigHelp[Foo])
}
