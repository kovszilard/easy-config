package easyconfig

import easyconfig.Parser.createParser
import scala.util.Try

case class Secret(secretValue: String) {

  override def toString: String = "*****"
}

object Secret {

  implicit val secretParser = createParser( str => Try(Secret(str))): Parser[Secret]

}