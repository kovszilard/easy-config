package easyconfig

trait Parser[A] {
  def parse(str: String): A
}

object Parser {

  def createParser[A](f: String => A): Parser[A] = new Parser[A] {
    def parse(str: String): A = f(str)
  }

  implicit val stringParser = createParser(identity)

  implicit val byteParser = createParser(s => s.toByte)
  implicit val shortParser = createParser(s => s.toShort)
  implicit val intParser = createParser(s => s.toInt)
  implicit val longParser = createParser(s => s.toLong)

  implicit val floatParser = createParser(s => s.toFloat)
  implicit val doubleParser = createParser(s => s.toDouble)

  implicit val booleanParser = createParser(s => s.toBoolean)

  implicit def listParser[A](implicit elementParser: Parser[A]): Parser[List[A]] = createParser { s =>
    s.split(",").toList.map(elementParser.parse)
  }
  implicit def seqParser[A](implicit listParser: Parser[List[A]]): Parser[Seq[A]] = createParser{ s =>
    listParser.parse(s).toSeq
  }
  implicit def vectorParser[A](implicit listParser: Parser[List[A]]): Parser[Vector[A]] = createParser{ s =>
    listParser.parse(s).toVector
  }
  implicit def setParser[A](implicit listParser: Parser[List[A]]): Parser[Set[A]] = createParser{ s =>
    listParser.parse(s).toSet
  }

}
