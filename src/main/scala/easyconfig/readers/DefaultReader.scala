package easyconfig.readers

import easyconfig.helpers._
import shapeless._
import shapeless.ops.hlist._

trait DefaultReader[A] {
  type Out
  def readDefault: Out
}

object DefaultReader {

  type Aux[I, O] = DefaultReader[I] { type Out = O }

  def apply[A](implicit defaultReader: DefaultReader[A]): Aux[A, defaultReader.Out] = defaultReader

  object tuple2Either extends Poly1 {
    implicit def allCase[A, K <: Symbol, V]: Case.Aux[(String, Option[A]), Either[DefaultReaderError, A]] = at {
      case (_, Some(value)) => Right(value)
      case (name, None) => Left(DefaultNotFound(name))
    }
  }

  implicit def defaultReader[A, FNO <: HList, DO <: HList, ZO <: HList, MO <: HList]
  (implicit
   default: Default.AsOptions.Aux[A, DO],
   fieldNames: FieldNames.Aux[A, FNO],
   zipper: Zip.Aux[FNO :: DO :: HNil, ZO],
   mapper: Mapper.Aux[tuple2Either.type, ZO, MO]
  ): Aux[A, MO] =
    new DefaultReader[A] {
      type Out = MO

      def readDefault: Out = zipper.apply(fieldNames.apply() :: default.apply() :: HNil).map(tuple2Either)
    }
}
