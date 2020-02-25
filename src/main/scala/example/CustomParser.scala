package example

import scala.util.Try
import easyconfig._
import easyconfig.Parser
import easyconfig.Parser.createParser

object CustomParser extends App {

  case class Name(first: String, last: String)

  implicit val nameParser: Parser[Name] = createParser{ str =>
    Try{
      val fullname = str.split(" ")
      Name(fullname(0), fullname(1))
    }
  }

  case class Config(name: Name)

  val config = easyConfig[Config](args)

  config.fold(
    help => println(help),
    config => //do something with the configuration
      println(s"Configuration: $config\n")
  )
}
