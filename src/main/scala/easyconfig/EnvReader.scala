package easyconfig

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

  def fieldNameToEnvVar(name: String): String = name.flatMap( c => if(c.isUpper) List('_', c) else List(c)).mkString.toUpperCase
  def fieldNameToParam(name: String): String = "--" + name.flatMap( c => if(c.isUpper) List('-', c) else List(c)).mkString.toLowerCase

  def getEnv[A](envVal: String)(implicit parser: Parser[A]): Either[AllErrors, A] = {
    val envValValue = sys.env.get(envVal)
    envValValue match {
      case Some(v) => parser.parse(v) match {
        case Failure(exception) => Left(EnvVarParseError(envVal, exception.getMessage))
        case Success(value) => Right(value)
      }
      case None => Left(EnvVarNotFound(envVal))
    }

  }

  def apply[A](implicit envReader: EnvReader[A]): Aux[A, envReader.Out] = envReader

  implicit val hnilEnvReader: Aux[HNil, HNil] = new EnvReader[HNil] {
    type Out = HNil

    def readEnv: Out = HNil
  }

  implicit def fieldTypeEnvReader[K <: Symbol, V](implicit witness: Witness.Aux[K], parser: Parser[V]): Aux[FieldType[K, V], Either[AllErrors, V]] =
    new EnvReader[FieldType[K, V]] {
      type Out = Either[AllErrors, V]

      def readEnv: Out = getEnv(fieldNameToEnvVar(witness.value.name))
    }

//  implicit def hHNil[H , HO, TO](implicit
//                                                               hEnvReader: Lazy[EnvReader.Aux[H, HO]],
//                                                               tEnvReader: EnvReader.Aux[HNil, TO]): Aux[H :: HNil, HO :: TO] =
//    new EnvReader[H :: HNil] {
//      type Out = HO :: Either[AllErrors, HNil]
//
//      def readEnv: Out = hEnvReader.value.readEnv :: Right(HNil)
//    }

  implicit def hlistEnvReader[H , HO, T <: HList, TO <: HList](implicit
                                                              hEnvReader: Lazy[EnvReader.Aux[H, HO]],
                                                              tEnvReader: EnvReader.Aux[T, TO]): Aux[H :: T, HO :: TO] =
    new EnvReader[H :: T] {
      type Out = HO :: TO

      def readEnv: Out = hEnvReader.value.readEnv :: tEnvReader.readEnv
    }

  implicit def genericEnvReader[A, R, RO](implicit generic: LabelledGeneric.Aux[A, R], envReader: Lazy[EnvReader.Aux[R, RO]]): Aux[A, RO] = new EnvReader[A] {
    type Out = RO

    def readEnv: Out = envReader.value.readEnv
  }

}
