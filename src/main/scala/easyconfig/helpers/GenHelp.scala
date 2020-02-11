package easyconfig.helpers

import easyconfig.readers._
import shapeless.HList
import shapeless.ops.hlist.ToTraversable

trait GenHelp[A] {
  def genHelp: String
}

object GenHelp {

  def apply[A](implicit gh: GenHelp[A]): GenHelp[A] = gh

  implicit def genHelp[A, O <: HList, DO <: HList](implicit
                                      fieldNames: FieldNames.Aux[A, O],
                                      toTraversable: ToTraversable.Aux[O, List, String]): GenHelp[A] = new GenHelp[A] {

    def genHelp: String = {
      val fields = fieldNames.apply().toList
      val args = fields.map(ArgReader.fieldNameToArg).mkString("\n")
      val envVars = fields.map(EnvReader.fieldNameToEnvVar).mkString("\n")

      s"""|Please provide configuration with the following environment variables:
          |$envVars
          |
          |Or override with the following command line arguments:
          |$args
          |""".stripMargin
    }

  }
}
