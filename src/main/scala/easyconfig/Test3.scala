package easyconfig

import EnvReader._
import shapeless._
import shapeless.labelled.FieldType

object Test3 extends App {

  case class Foo(foo: String)

  implicitly[EnvReader[HNil]]

  val generic = LabelledGeneric[Foo]

  val s = Symbol("foo")

  implicitly[EnvReader[FieldType[s.type, Int]]]
  implicitly[EnvReader[FieldType[s.type, String]]]
  implicitly[EnvReader[FieldType[s.type, String]] :: HNil]
  println(implicitly[EnvReader[FieldType[s.type, String]]].readEnv)
  implicitly[EnvReader[HNil]]
//  implicitly[EnvReader[generic.Repr]]

//  println(EnvReader[Foo])
}
