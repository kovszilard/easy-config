package easyconfig

import shapeless._

trait Done[A] {
  def getConfig(args: List[String]): Either[AllError, A]
}

object Done {

  def apply[A](implicit done: Done[A]): Done[A] = done

  implicit def done[A, ARepr <: HList, ERepr <: HList](implicit
                                       gen: Generic.Aux[A, ARepr],
                                       easyConfig: EasyConfig.Aux[A, Either[AllError, ARepr]]): Done[A] = new Done[A] {

    def getConfig(args: List[String]): Either[AllError, A] = {
      easyConfig.getConfig(args).map( hl => gen.from(hl))
    }

  }



}
