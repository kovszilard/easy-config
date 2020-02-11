package easyconfig.helpers

import shapeless._
import shapeless.labelled.FieldType

trait FieldNames[A] {
  type Out
  def apply(): Out
}

object FieldNames {

  type Aux[I, O] = FieldNames[I] { type Out = O }

  def apply[A](implicit fieldNames: FieldNames[A]): Aux[A, fieldNames.Out] = fieldNames

  implicit val hNilFieldName: Aux[HNil, HNil] = new FieldNames[HNil] {
    type Out = HNil

    def apply(): Out = HNil
  }

  implicit def fieldTypeFieldNames[K <: Symbol, V](implicit witness: Witness.Aux[K]): Aux[FieldType[K, V], String] =
    new FieldNames[FieldType[K, V]] {
      type Out = String
      def apply(): Out = witness.value.name
    }

  implicit def hListFieldNames[H, HO,  T <: HList, TO <: HList](implicit
                                                                hFieldNames: Lazy[Aux[H, HO]],
                                                                tFieldNames: Aux[T, TO]
                                                               ): Aux[H :: T, HO :: TO] =
    new FieldNames[H :: T] {
      type Out = HO :: TO
      def apply: Out = hFieldNames.value.apply :: tFieldNames.apply
    }

  implicit def genericFieldNames[A, AO <: HList, O <: HList](implicit
                                                             gen: LabelledGeneric.Aux[A, AO],
                                                             fieldNames: Lazy[Aux[AO, O]]
                                                            ): Aux[A, O] =
    new FieldNames[A] {
      type Out = O
      def apply(): Out = fieldNames.value.apply
    }
}
