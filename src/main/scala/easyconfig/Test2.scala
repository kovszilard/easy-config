//package easyconfig
//
//import shapeless._
//import shapeless.labelled._
//import shapeless.syntax.SingletonOps
//import shapeless.syntax.singleton._
//
//case class Baz(x: Int = 1)
//
//object Test2 extends App {
//
//  implicitly[EnvReader2[HNil]]
//
//  val s: Symbol = 'num
//
//  val h = (s ->> 42)
//
//  val gen = LabelledGeneric[Foo]
//  val h2 = gen.to(Foo(42)).head
//
//  val n = Symbol("num")
//  type K = n.type
//  val record2 = field[K](42) :: HNil
//
////  implicitly[EnvReader2[record2.type ]]
//
//  println(implicitly[EnvReader2[Foo]].readEnv)
//  println(implicitly[EnvReader2[Bar]].readEnv)
////  println(implicitly[EnvReader2[Baz]].readEnv)
//
//}
//
//object Test3 extends App {
//
//  case class Apple(color: String, size: Int)
//
//  val apple = implicitly[EnvReader2[Apple]].readEnv
//  println(apple)
//}
