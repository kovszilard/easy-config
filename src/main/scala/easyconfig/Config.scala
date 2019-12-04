package easyconfig

import shapeless._
import shapeless.record._
import shapeless.labelled._

object Config {

  def readFromEnv[A](implicit envReader: EnvReader[A]): envReader.Out = envReader.readEnv

  def readFromEnv2[A, O](implicit envReader: EnvReader.Aux[A, O], gen: Generic.Aux[A, O]): A = gen.from(readFromEnv[A])

  def readFromEnv3[A](hl: HList)(implicit gen: Generic.Aux[A, hl.type]): A = gen.from(hl)

}
