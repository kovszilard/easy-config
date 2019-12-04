package easyconfig

import shapeless._
import shapeless.labelled._

trait EnvReader[A] {
  type Out
  def readEnv: Out
}

object EnvReader {

  type Aux[I, O] = EnvReader[I] { type Out = O }

  def fieldNameToEnvVar(name: String): String = name.flatMap( c => if(c.isUpper) List('_', c) else List(c)).mkString.toUpperCase
  def fieldNameToParam(name: String): String = "--" + name.flatMap( c => if(c.isUpper) List('-', c) else List(c)).mkString.toLowerCase


  implicit val hnilEnvReader: Aux[HNil, HNil] = new EnvReader[HNil] {
    type Out = HNil

    def readEnv: Out = HNil
  }

  implicit def fieldTypeEnvReader[K <: Symbol, V](implicit witness: Witness.Aux[K], parser: Parser[V]): Aux[FieldType[K, V], V] =
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
