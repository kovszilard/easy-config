package easyconfig.readers

import shapeless._
import shapeless.labelled._

import scala.util.{Failure, Success}

// trait EnvReader[K, V, A <: FieldType[K, V]] {
//  def readEnv: V
// }

trait EnvReader[A] {
  type Out
  def readEnv: Out
}

object EnvReader {

  type Aux[I, O] = EnvReader[I] { type Out = O }

  def apply[A](implicit envReader: EnvReader[A]): Aux[A, envReader.Out] = envReader

  private[easyconfig] def fieldNameToEnvVar(name: String): String = name.flatMap( c => if(c.isUpper) List('_', c) else List(c)).mkString.toUpperCase

  private def getEnv[A](fieldName: String)(implicit parser: Parser[A]): Either[EnvReaderError, A] = {
    val envValValue = sys.env.get(fieldNameToEnvVar(fieldName))
    envValValue match {
      case Some(v) => parser.parse(v) match {
        case Failure(exception) => Left(EnvParseError(fieldName, fieldNameToEnvVar(fieldName), exception.getMessage))
        case Success(value) => Right(value)
      }
      case None => Left(EnvNotFound(fieldName, fieldNameToEnvVar(fieldName)))
    }

  }

  implicit val hnilEnvReader: Aux[HNil, HNil] = new EnvReader[HNil] {
    type Out = HNil

    def readEnv: Out = HNil
  }

  implicit def fieldTypeEnvReader[K <: Symbol, V](implicit witness: Witness.Aux[K], parser: Parser[V]): Aux[FieldType[K, V], Either[EnvReaderError, V]] =
    new EnvReader[FieldType[K, V]] {
      type Out = Either[EnvReaderError, V]

      def readEnv: Out = getEnv(witness.value.name)
    }

  implicit def hlistEnvReader[H , HO, T <: HList, TO <: HList](implicit
                                                              hEnvReader: Lazy[EnvReader.Aux[H, HO]],
                                                              tEnvReader: EnvReader.Aux[T, TO]): Aux[H :: T, HO :: TO] =
    new EnvReader[H :: T] {
      type Out = HO :: TO

      def readEnv: Out = hEnvReader.value.readEnv :: tEnvReader.readEnv
    }

  implicit def genericEnvReader[A, R, RO](implicit generic: LabelledGeneric.Aux[A, R], envReader: Lazy[EnvReader.Aux[R, RO]]): Aux[A, RO] =
    new EnvReader[A] {
      type Out = RO

      def readEnv: Out = envReader.value.readEnv
    }

}
