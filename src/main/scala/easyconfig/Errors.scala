package easyconfig

sealed trait AllErrors
case class EnvVarParseError(varName: String, msg: String) extends AllErrors
case class EnvVarNotFound(varName: String) extends AllErrors
