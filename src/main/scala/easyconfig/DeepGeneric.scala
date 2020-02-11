package easyconfig

import shapeless._
import shapeless.labelled._

trait DeepGeneric[T] {
  type Repr <: HList
  def to(t: T): Repr
}

trait LowPriority {
  implicit def plainHconsDeepGeneric[H, T <: HList](
                                                     implicit
                                                     tl: DeepGeneric[T]
                                                   ) = new DeepGeneric[H :: T] {
    def to(t: H :: T): Repr = t.head :: tl.to(t.tail)
    type Repr = H :: tl.Repr
  }
}

object DeepGeneric extends LowPriority {

  type Aux[A, R] = DeepGeneric[A] { type Repr = R }
  def apply[A : DeepGeneric]: DeepGeneric[A] = implicitly[DeepGeneric[A]]

  implicit val hnilDeepGeneric = new DeepGeneric[HNil] {
    def to(a: HNil) = a
    type Repr = HNil
  }

  implicit def nestedHconsDeepGeneric[K, H, T <: HList, RH <: HList, RT <: HList](
                                                                                   implicit
                                                                                   dg: Lazy[DeepGeneric.Aux[H, RH]],
                                                                                   tl: Lazy[DeepGeneric.Aux[T, RT]]
                                                                                 ) = new DeepGeneric[FieldType[K, H] :: T] {
    type Repr = FieldType[K, RH] :: RT
    def to(t: FieldType[K, H] :: T): Repr = {
      labelled.field[K](dg.value.to(t.head)) :: tl.value.to(t.tail)
    }
  }

  implicit def genericDeepGeneric[A, R <: HList](
                                                  implicit
                                                  g: LabelledGeneric.Aux[A, R],
                                                  d: DeepGeneric[R]
                                                ) = new DeepGeneric[A] {
    def to(a: A) = d.to(g.to(a))
    type Repr = d.Repr
  }
}