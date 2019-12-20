package easyconfig

import EnvReader._
import shapeless._
import shapeless.labelled.FieldType

object Test extends App {

  case class Foo(str: String, num: Int)


  val generic = LabelledGeneric[Foo]

  val sStr = Symbol("str")
  val sNum = Symbol("num")

  implicitly[EnvReader[HNil]]
  implicitly[EnvReader[FieldType[sStr.type, Int]]]
  implicitly[EnvReader[FieldType[sStr.type, String]]]
//  implicitly[EnvReader[FieldType[sNum.type, Int] :: FieldType[sStr.type, String]]]
  implicitly[EnvReader[FieldType[sStr.type, String] :: HNil]]

  println(implicitly[EnvReader[FieldType[sStr.type, String]]].readEnv)
  println(implicitly[EnvReader[FieldType[sNum.type, Int]]].readEnv)

  println(implicitly[EnvReader[FieldType[sNum.type, Int] :: HNil]].readEnv)

  println(implicitly[EnvReader[FieldType[sStr.type, String] :: FieldType[sNum.type, Int] :: HNil]].readEnv)
  
  println(implicitly[EnvReader[Foo]].readEnv)

}
