package easyconfig

import shapeless._

trait ConfigRequest[A] {
  def getConfig(args: List[String]): Either[AllError, A]
}

object ConfigRequest {

  def apply[A](implicit done: ConfigRequest[A]): ConfigRequest[A] = done

  implicit def typeRequest[A, ARepr <: HList, ERepr <: HList](implicit
                                       gen: Generic.Aux[A, ARepr],
                                       easyConfig: CompoundReader.Aux[A, Either[AllError, ARepr]]): ConfigRequest[A] = new ConfigRequest[A] {

    def getConfig(args: List[String]): Either[AllError, A] = {
      easyConfig.getConfig(args).map( hl => gen.from(hl))
    }

  }



}
