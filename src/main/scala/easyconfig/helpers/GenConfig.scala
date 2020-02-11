package easyconfig.helpers

import easyconfig.readers._
import shapeless._

trait GenConfig[A] {
  def getConfig(args: List[String]): Either[List[ReaderError], A]
}

object GenConfig {

  def apply[A](implicit genConfig: GenConfig[A]): GenConfig[A] = genConfig

  implicit def typeRequest[A, ARepr <: HList, ERepr <: HList](implicit
                                       gen: Generic.Aux[A, ARepr],
                                       easyConfig: CompoundReader.Aux[A, Either[List[ReaderError], ARepr]]): GenConfig[A] = new GenConfig[A] {

    def getConfig(args: List[String]): Either[List[ReaderError], A] = {
      easyConfig.getConfig(args).map( hl => gen.from(hl))
    }
  }
}
