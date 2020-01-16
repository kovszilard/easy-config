package easyconfig

import shapeless._
import shapeless.ops.hlist._
import shapeless.ops._

trait DefaultReader[A] {
  type Out
  def readDefault: Out
}

object DefaultReader {

  type Aux[I, O] = DefaultReader[I] { type Out = O }

  def apply[A](implicit defaultReader: DefaultReader[A]): Aux[A, defaultReader.Out] = defaultReader

  object opt2Either extends Poly1 {
    implicit def allCase[A]: Case.Aux[Option[A], Either[DefaultReaderError, A]] = at {
      case Some(value) => Right(value)
      case None => Left(DefaultNotFound("TODO"))
    }
  }

  implicit def defaultReader[A, DO <: HList, MO <: HList]
    (implicit
       default: Default.AsOptions.Aux[A, DO],
       mapper: Mapper.Aux[opt2Either.type, DO, MO]
    ): Aux[A, MO] =
    new DefaultReader[A] {
      type Out = MO

      def readDefault: Out = default.apply().map(opt2Either)
    }
}
