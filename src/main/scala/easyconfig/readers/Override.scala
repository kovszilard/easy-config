package easyconfig.readers

import shapeless._
import shapeless.ops.hlist.{Mapper, Zip}

trait Override[A <: HList, B <: HList] {
  type Out
  def overrideLeft(a: A, b: B): Out
}

object Override {

  type Aux[A <: HList, B <: HList, O] = Override[A, B] { type Out = O }

  def apply[A <: HList, B <: HList](implicit o: Override[A, B]): Aux[A, B, o.Out] = o

  object polyOverride extends Poly1 {
    implicit def case1[A, EL <: ReaderError, ER <: ReaderError]: Case.Aux[(Either[EL, A], Either[ER, A]), Either[List[ReaderError], A]] =
      at{
        case (Right(_), Right(b)) => Right(b)
        case (Right(a), Left(_)) => Right(a)
        case (Left(e :ParseError), Right(_)) => Left(List(e))
        case (Left(_), Right(b)) => Right(b)
        case (Left(a), Left(b)) => Left(List(a, b))
      }

    implicit def case2[A, EL <: ReaderError, ER <: ReaderError]: Case.Aux[(Either[List[EL], A], Either[ER, A]), Either[List[ReaderError], A]] =
      at{
        case (Right(_), Right(b)) => Right(b)
        case (Right(a), Left(_)) => Right(a)
        case (Left(_), Right(b)) => Right(b)
        case (Left(a), Left(b)) => Left(a.:+(b))
      }
  }

  object polyOverride2 extends Poly1 {
    implicit def case1[A, EL <: ReaderError, ER <: ReaderError]: Case.Aux[(Either[EL, A], Either[ER, A]), Either[ReaderError, A]] =
      at{
        case (Right(_), Right(b)) => Right(b)
        case (Right(_), Left(e :ParseError)) => Left(e)
        case (Right(a), Left(_)) => Right(a)

        case (Left(e :ParseError), Right(_)) => Left(e)
        case (Left(_), Right(b)) => Right(b)

        case (Left(e :ParseError), Left(_ :ParseError)) => Left(e)
        case (Left(e), Left(e2 :ParseError)) if !e.isInstanceOf[ParseError] => Left(e2)
        case (Left(e1), Left(_)) => Left(e1)
      }

  }

  implicit def overrider[A <: HList, B <: HList, Combined <: HList, O <: HList]
    (implicit
      zip: Zip.Aux[A :: B :: HNil, Combined],
      mapper: Mapper.Aux[polyOverride2.type, Combined, O]
    ): Aux[A, B, O] =
    new Override[A, B] {
      type Out = O

      def overrideLeft(a: A, b: B): Out = a.zip(b).map(polyOverride2)
    }

}