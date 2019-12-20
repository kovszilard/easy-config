//package easyconfig
//
//import Config._
//import shapeless._
//
//case class Foo(num: Int)
//case class Bar(num: Int, str: String)
//
//object Test extends App {
//
//  println("Foo from env: " + readFromEnv[Foo])
//  println("Bar from env: " + readFromEnv[Bar])
//
////  println("Foo from env: " + config[Foo])
////  println("Bar from env: " + config[Bar])
//
//  val fooGen = readFromEnv[Foo]
//
//  val barGen = readFromEnv[Bar]
//
//  println(Generics2CaseClass[Foo].convert(fooGen))
//  println(Generics2CaseClass[Bar].convert(barGen))
//
//  println(Generics2CaseClass[Foo].convert(readFromEnv[Foo]))
//  println(Generics2CaseClass[Bar].convert(readFromEnv[Bar]))
//
//  import EnvReader._
//
//  implicitly[EnvReader[Foo]]
//
//  def c = {
//    Generics2CaseClass[Foo].convert(readFromEnv[Foo])
//  }
//
//
////  def a[A]: A = {
//////    val e = EnvReader[A]
//////    val g = Generic[A]
////    Generic[A].from(EnvReader[A].readEnv)
////  }
//
////  def r[A, X <: HList](implicit envReader: EnvReader.Aux[A, X], generic: Generic.Aux[A, X], ev:  ): A = generic.from(envReader.readEnv)
////  r[Foo]
//
//}
