package easyconfig

import easyconfig.EnvReader._
import shapeless._
import shapeless.ops.hlist._
import shapeless.ops._

object Test2 extends App {
  import ConfigReader._

  case class Foo(str: String, num: Int = 100)

//  val config = ConfigReader[Foo].readConfig
//  println(config)

//  object folderTest extends Poly2 {
//    implicit val allCase: Case.Aux[Int, Int, Int] = at( (a, b) => a + b)
//  }
//
//  object foldLTest extends Poly2 {
//    implicit def allCase[ACC <: HList, B]: Case.Aux[ACC, B, B :: ACC] = at((acc, b) => b :: acc )
//  }
//
//  object foldRTest extends Poly2 {
//    implicit def allCase[ACC <: HList, B]: Case.Aux[B, ACC, B :: ACC] = at((b, acc) => b :: acc )
//  }

//
//  println((1 :: HNil).foldLeft(0)(folderTest))
//  println((1 :: 2 :: HNil).foldLeft(0)(folderTest))
//
//  println((1 :: HNil).foldLeft(HNil)(foldLTest))
//  println((1 :: 2 :: HNil).foldLeft(HNil)(foldLTest))
//
//  println((1 :: 2 :: HNil).foldRight(HNil)(foldRTest))

//  object folder extends Poly2 {
//    implicit def allCase[A, ACC <: HList]: Case.Aux[Either[Error, A], Either[Error, ACC], Either[Error, A :: ACC]] =
//      at{ (a, acc) =>
//        acc match {
//          case Left(error) => Left(error)
//          case Right(l) => a match {
//            case Right(r) => Right(r :: l)
//            case Left(err) => Left(err)
//          }
//        }
//      }
//  }
//
//  println(config.foldRight(Right(HNil): Either[Error, HNil])(folder))
//  val e = config.foldRight(Right(HNil): Either[Error, HNil])(folder)
//
//  println(e.map( right => Generic[Foo].from(right)))

//  val c = EasyConfig[Foo].getConfig(args.toList)
  println(implicitly[Default.AsOptions[Foo]].apply())

  import DefaultReader._

}

trait ConfigReader[A] {
  type Out
  def readConfig[A]: Out
}

object ConfigReader {
  type Aux[I, O] = ConfigReader[I] { type Out = O }

  object envOrDefault extends Poly1 {
    implicit def allCase[A]: Case.Aux[(Either[Error, A], Option[A]), Either[Error, A]] =
      at{
        case (Right(x), _) => Right(x)
        case (Left(_), Some(y)) => Right(y)
        case (Left(err), None) => Left(err)
      }
  }

  def apply[I](implicit cr: ConfigReader[I]): Aux[I, cr.Out] = cr

  implicit def configReader[A, EO <: HList, DO <: HList, Combined <: HList, MO <: HList](implicit
                                                    envReader: EnvReader.Aux[A, EO],
                                                    default: Default.AsOptions.Aux[A, DO],
                                                    zip: Zip.Aux[EO :: DO :: HNil, Combined],
                                                    mapper: Mapper.Aux[envOrDefault.type, Combined, MO]): Aux[A, MO] =
    new ConfigReader[A] {
      type Out = MO

      def readConfig[A]: Out = {
        val fromEnv = envReader.readEnv
        val default1 = default.apply()

        val zipped = fromEnv.zip(default1)
        zipped.map(envOrDefault)
      }
    }
}