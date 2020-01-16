package easyconfig

import shapeless._
import shapeless.ops.hlist.Zip
import shapeless.ops.hlist.Mapper

trait Override[A <: HList, B <: HList] {
  type Out
  def overrideLeft(a: A, b: B): Out
}

object Override {

  type Aux[A <: HList, B <: HList, O] = Override[A, B] { type Out = O }

  def apply[A <: HList, B <: HList](implicit o: Override[A, B]): Aux[A, B, o.Out] = o

  object polyOverride extends Poly1 {
    implicit def allCase[A, EL <: AllError, ER <: AllError]: Case.Aux[(Either[EL, A], Either[ER, A]), Either[AllError, A]] =
      at{
        case (Right(_), Right(b)) => Right(b)
        case (Right(a), Left(_)) => Right(a)
        case (Left(_), Right(b)) => Right(b)
        case (Left(a), Left(_)) => Left(a)
      }
  }

  implicit def overrider[A <: HList, B <: HList, Combined <: HList, O <: HList]
    (implicit
      zip: Zip.Aux[A :: B :: HNil, Combined],
      mapper: Mapper.Aux[polyOverride.type, Combined, O]
    ): Aux[A, B, O] =
    new Override[A, B] {
      type Out = O

      def overrideLeft(a: A, b: B): Out = a.zip(b).map(polyOverride)
    }

}