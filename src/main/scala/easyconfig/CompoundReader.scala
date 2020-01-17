package easyconfig

import shapeless._
import shapeless.ops.hlist.RightFolder
import shapeless.ops.hlist.RightReducer
trait CompoundReader[A] {
  type Out

  def getConfig(args: List[String]): Out
}

object CompoundReader {

  type Aux[I, O] = CompoundReader[I] { type Out = O }

  def apply[A](implicit compoundReader: CompoundReader[A]): Aux[A, compoundReader.Out] = compoundReader

  object folder extends Poly2 {
    implicit def allCase[E <: AllError, A, ACC <: HList]: Case.Aux[Either[E, A], Either[E, ACC], Either[E, A :: ACC]] =
      at { (a, acc) =>
        acc match {
          case Left(e) => Left(e)
          case Right(hl) => a match {
            case Right(value) => Right(value :: hl)
            case Left(e) => Left(e)
          }
        }
      }
  }

  implicit def compoundReader[A,
                          DO <: HList,
                          EO <: HList,
                          ODO <: HList,
                          AO <: HList,
                          O <: HList,
                          FO <: Either[AllError, HList]
                          ]
    (implicit
      dr: DefaultReader.Aux[A, DO],
      er: EnvReader.Aux[A, EO],
      overrideDefault: Override.Aux[DO, EO, ODO],
      ar: ArgReader.Aux[A, AO],
      overrideEnv: Override.Aux[ODO, AO, O],
      rightFolder: RightFolder.Aux[O, Either[AllError, HNil], folder.type, FO]
    ): Aux[A, FO] =
    new CompoundReader[A] {
      type Out = FO

      def getConfig(args: List[String]): Out = {
        val o1 = overrideDefault.overrideLeft(dr.readDefault, er.readEnv)
        val o2 = overrideEnv.overrideLeft(o1, ar.readArgs(args))
        rightFolder.apply(o2, Right(HNil))
      }
    }
}
