package easyconfig.readers

import scala.util.Try

trait Parser[A] {
  def parse(str: String): Try[A]
}

object Parser {

  def createParser[A](f: String => Try[A]): Parser[A] = new Parser[A] {
    def parse(str: String): Try[A] = f(str)
  }

  implicit val stringParser = createParser(s => Try(s))

  implicit val byteParser = createParser(s => Try(s.toByte))
  implicit val shortParser = createParser(s => Try(s.toShort))
  implicit val intParser = createParser(s => Try(s.toInt))
  implicit val longParser = createParser(s => Try(s.toLong))

  implicit val floatParser = createParser(s => Try(s.toFloat))
  implicit val doubleParser = createParser(s => Try(s.toDouble))

  implicit val booleanParser = createParser(s => Try(s.toBoolean))

//  implicit def listParser[A](implicit elementParser: Parser[A]): Parser[List[A]] = createParser { s =>
//    s.split(",").toList.map(elementParser.parse)
//  }
//  implicit def seqParser[A](implicit listParser: Parser[List[A]]): Parser[Seq[A]] = createParser{ s =>
//    listParser.parse(s).toSeq
//  }
//  implicit def vectorParser[A](implicit listParser: Parser[List[A]]): Parser[Vector[A]] = createParser{ s =>
//    listParser.parse(s).toVector
//  }
//  implicit def setParser[A](implicit listParser: Parser[List[A]]): Parser[Set[A]] = createParser{ s =>
//    listParser.parse(s).toSet
//  }

}
