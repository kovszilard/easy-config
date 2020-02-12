package easyconfig

object Test extends App {

  case class Foo(bar: Int, baz: String = "default baz")

  val config = easyConfig[Foo](args)

  config.fold(println, println)

}
