package easyconfig

import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context

object Macros {
  def impl[T](c: Context)(T: c.WeakTypeTag[T]): c.Expr[Map[String, Any]] = {
    import c.universe._
    val classSym = T.tpe.typeSymbol
    val moduleSym = classSym.companionSymbol
    val apply = moduleSym.typeSignature.declaration(newTermName("apply")).asMethod
    // can handle only default parameters from the first parameter list
    // because subsequent parameter lists might depend on previous parameters
    val kvps = apply.paramss.head.map(_.asTerm).zipWithIndex.flatMap{ case (p, i) =>
      if (!p.isParamWithDefault) None
      else {
        val getterName = newTermName("apply$default$" + (i + 1))
        Some(q"${p.name.toString} -> $moduleSym.$getterName")
      }
    }
    c.Expr[Map[String, Any]](q"Map[String, Any](..$kvps)")
  }

  def extractor[T]: Map[String, Any] = macro impl[T]
}
