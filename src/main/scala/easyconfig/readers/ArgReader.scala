package easyconfig.readers

import easyconfig.Parser
import shapeless._
import shapeless.labelled.FieldType

import scala.util.{Failure, Success}

trait ArgReader[A] {
  type Out
  def readArgs(args: List[String]): Out
}

object ArgReader {

  type Aux[I, O] = ArgReader[I] { type Out = O }

  def apply[A](implicit argsReader: ArgReader[A]): Aux[A, argsReader.Out] = argsReader

  private[easyconfig] def fieldNameToArg(name: String): String = "--" + name.flatMap( c => if(c.isUpper) List('-', c) else List(c)).mkString.toLowerCase

  private def argsList2Map(args: List[String]): Map[String, String] = {
    args.sliding(2, 2).flatMap{
      case List(a, b) => List((a, b))
      case _ => Nil
    }.toMap
  }

  private def getArg[A](args: List[String], fieldName: String)(implicit parser: Parser[A]): Either[ArgReaderError, A] = {
    argsList2Map(args).get(fieldNameToArg(fieldName)) match {
      case Some(value) => parser.parse(value) match {
        case Failure(exception) => Left(ArgParseError(fieldName, fieldNameToArg(fieldName), exception.getMessage))
        case Success(value) => Right(value)
      }
      case None => Left(ArgNotFound(fieldName, fieldNameToArg(fieldName)))
    }
  }

  implicit val hNilArgReader: Aux[HNil, HNil] =
    new ArgReader[HNil] {
      type Out = HNil

      def readArgs(args: List[String]): HNil = HNil
    }

  implicit def fieldTypeArgReader[K <: Symbol, V](implicit witness: Witness.Aux[K], parser: Parser[V]): Aux[FieldType[K, V], Either[ArgReaderError, V]] =
    new ArgReader[FieldType[K, V]] {
      type Out = Either[ArgReaderError, V]

      def readArgs(args: List[String]): Out = getArg(args, witness.value.name)
    }

  implicit def hlistArgReader[H , HO, T <: HList, TO <: HList](implicit
                                                               hArgReader: Lazy[ArgReader.Aux[H, HO]],
                                                               tArgReader: ArgReader.Aux[T, TO]): Aux[H :: T, HO :: TO] =
    new ArgReader[H :: T] {
      type Out = HO :: TO

      def readArgs(args: List[String]): Out = hArgReader.value.readArgs(args) :: tArgReader.readArgs(args)
    }

  implicit def genericArgReader[A, R, RO](implicit generic: LabelledGeneric.Aux[A, R], argReader: Lazy[ArgReader.Aux[R, RO]]): Aux[A, RO] =
    new ArgReader[A] {
      type Out = RO

      def readArgs(args: List[String]): Out = argReader.value.readArgs(args)
    }
}
