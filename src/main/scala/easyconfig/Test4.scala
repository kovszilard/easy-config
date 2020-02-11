package easyconfig

object Test4 extends App {

  case class Foo(num: Option[Int], helloWorld: String)// = "default")

  println(easyConfig[Foo](List.empty))
  println(easyConfig[Foo](List("--num", "1", "--hello-world", "X")))
  println(easyConfig[Foo](List("--num", "1.2","--hello-world", "X")))
}
