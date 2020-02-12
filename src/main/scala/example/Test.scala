package example

import easyconfig._

object Test extends App {

  case class Config(foo: Int, bar: String, baz: Option[List[Int]] = None)

  val config: Either[String, Config] = easyConfig[Config](args)

  config.fold(
    help => println(help),
    config => //do something with the configuration
      println(s"Configuration: $config")
  )

}
