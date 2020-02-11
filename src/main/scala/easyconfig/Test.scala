package easyconfig

import shapeless._
import readers._
import helpers._

object Test extends App {

  case class Foo(num: Int, str: String = "default")

  val fieldNames = FieldNames[Foo]
  println("fieldNames: " + fieldNames.apply)

  val defaultReader2 = DefaultReader[Foo]
  println("defaultReader2: " + defaultReader2.readDefault)


  case class Out(field1: Int, field2: In)
  case class In(field1: Int, field2: In2)
  case class In2(field1: Int)

  val test = Out(42, In(-42, In2(100)))

  val fieldNames2 = FieldNames[Out]
  val gen = Generic[Out]
  println("fieldNames2: " + fieldNames2.apply)
  println("gen: " + gen.to(test))

  val gen2 = DeepGeneric[Out]
  println("gen2: " + gen2.to(test))


}
