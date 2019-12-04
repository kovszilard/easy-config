package simpleconfig

import shapeless.{HList, ::, HNil, Lazy}
import shapeless._
import shapeless.record._
import shapeless.labelled._
//import syntax.singleton._

import scala.util.Try

//object Config {
//
//  trait Parser[A] {
//    def parse(str: String): Try[A]
//  }
//
//  object Parser {
//    def createParser[A](f: String => Try[A]): Parser[A] = new Parser[A] {
//      def parse(str: String): Try[A] = f(str)
//    }
//  }
//
//  implicit val stringParser = Parser.createParser(Try(_))
//
//  implicit val byteParser = Parser.createParser(s => Try(s.toByte))
//  implicit val shortParser = Parser.createParser(s => Try(s.toShort))
//  implicit val intParser = Parser.createParser(s => Try(s.toInt))
//  implicit val longParser = Parser.createParser(s => Try(s.toLong))
//
//  implicit val floatParser = Parser.createParser(s => Try(s.toFloat))
//  implicit val doubleParser = Parser.createParser(s => Try(s.toDouble))
//
//  implicit val booleanParser = Parser.createParser(s => Try(s.toBoolean))
//
//  implicit def listParser[A](implicit elementParser: Parser[A]): Parser[List[A]] = Parser.createParser { s =>
//    import cats.syntax.traverse._
//    import cats.instances.all._
//
//    val lt = s.split(",").toList.map(elementParser.parse)
//    lt.sequence
//  }
//  implicit def seqParser[A](implicit listParser: Parser[List[A]]): Parser[Seq[A]] = Parser.createParser{ s =>
//    listParser.parse(s).map(_.toSeq)
//  }
//  implicit def vectorParser[A](implicit listParser: Parser[List[A]]): Parser[Vector[A]] = Parser.createParser{ s =>
//    listParser.parse(s).map(_.toVector)
//  }
//  implicit def setParser[A](implicit listParser: Parser[List[A]]): Parser[Set[A]] = Parser.createParser{ s =>
//    listParser.parse(s).map(_.toSet)
//  }
//}

object Config {

  trait Parser[A] {
    def parse(str: String): A
  }

  object Parser {
    def createParser[A](f: String => A): Parser[A] = new Parser[A] {
      def parse(str: String): A = f(str)
    }
  }

  implicit val stringParser = Parser.createParser(identity)

  implicit val byteParser = Parser.createParser(s => s.toByte)
  implicit val shortParser = Parser.createParser(s => s.toShort)
  implicit val intParser = Parser.createParser(s => s.toInt)
  implicit val longParser = Parser.createParser(s => s.toLong)

  implicit val floatParser = Parser.createParser(s => s.toFloat)
  implicit val doubleParser = Parser.createParser(s => s.toDouble)

  implicit val booleanParser = Parser.createParser(s => s.toBoolean)

  implicit def listParser[A](implicit elementParser: Parser[A]): Parser[List[A]] = Parser.createParser { s =>
    s.split(",").toList.map(elementParser.parse)
  }
  implicit def seqParser[A](implicit listParser: Parser[List[A]]): Parser[Seq[A]] = Parser.createParser{ s =>
    listParser.parse(s).toSeq
  }
  implicit def vectorParser[A](implicit listParser: Parser[List[A]]): Parser[Vector[A]] = Parser.createParser{ s =>
    listParser.parse(s).toVector
  }
  implicit def setParser[A](implicit listParser: Parser[List[A]]): Parser[Set[A]] = Parser.createParser{ s =>
    listParser.parse(s).toSet
  }


  trait ExtractKV[A] {
    type Out
    def extract: Out
  }

  object ExtractKV {
    type Aux[A, O] = ExtractKV[A] {type Out = O}

    implicit val hnilExtract: Aux[HNil, HNil] = new ExtractKV[HNil] {
      type Out = HNil

      def extract = HNil
    }


  }


}

case class Foo(num: Int)
case class Bar(num: Int, str: String)

trait EnvReader[A] {
  type Out
  def readEnv: Out
}

object EnvReader {

  type Aux[I, O] = EnvReader[I] { type Out = O }

  def readFromEnv[A](implicit envReader: EnvReader[A]): envReader.Out = envReader.readEnv

  def readFromEnv2[A, O](implicit envReader: EnvReader.Aux[A, O], gen: Generic.Aux[A, O]): A = gen.from(readFromEnv[A])

  def readFromEnv3[A](hl: HList)(implicit gen: Generic.Aux[A, hl.type]): A = gen.from(hl)

  def fieldNameToEnvVar(name: String): String = name.flatMap( c => if(c.isUpper) List('_', c) else List(c)).mkString.toUpperCase
  def fieldNameToParam(name: String): String = "--" + name.flatMap( c => if(c.isUpper) List('-', c) else List(c)).mkString.toLowerCase



  implicit val hnilEnvReader: Aux[HNil, HNil] = new EnvReader[HNil] {
    type Out = HNil

    def readEnv: Out = HNil
  }

  implicit def fieldTypeEnvReader[K <: Symbol, V](implicit witness: Witness.Aux[K], parser: Config.Parser[V]): Aux[FieldType[K, V], V] =
    new EnvReader[FieldType[K, V]] {
      type Out = V

      def readEnv: Out = parser.parse(System.getenv(fieldNameToEnvVar(witness.value.name)))
    }


  implicit def hlistEnvReader[H, HO, T <: HList, TO <: HList](implicit hEnvReader: EnvReader.Aux[H, HO], tEnvReader: EnvReader.Aux[T, TO]): Aux[H :: T, HO :: TO] =
    new EnvReader[H :: T] {
      type Out = HO :: TO

      def readEnv: Out = hEnvReader.readEnv :: tEnvReader.readEnv
    }

  implicit def genericEnvReader[A, R, RO](implicit generic: LabelledGeneric.Aux[A, R], envReader: EnvReader.Aux[R, RO]): Aux[A, RO] = new EnvReader[A] {
    type Out = RO

    def readEnv: Out = envReader.readEnv
  }

}


object Test extends App {

  import EnvReader._

  println("Foo from env: " + readFromEnv[Foo])

  println("Bar from env: " + readFromEnv3[Bar](readFromEnv[Bar]))

  val barGen = readFromEnv[Bar]

//  println("Bar from env: " + a[Bar](barGen))


}