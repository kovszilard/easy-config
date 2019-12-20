//package easyconfig
//
//object TestMacro extends App {
//
//  case class Def(x: Int = 1, y: Int, z: String = "hello")
//
//  val m = Macros.extractor[Def]
//
//  println(m.get("x").map(_.asInstanceOf[Int]))
//  println(m.get("y").map(_.asInstanceOf[Int]))
//  println(m.get("z").map(_.asInstanceOf[String]))
//
//}
