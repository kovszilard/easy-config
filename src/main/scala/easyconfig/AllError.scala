package easyconfig

sealed trait AllError

sealed trait DefaultReaderError extends AllError
case class DefaultNotFound(varName: String) extends DefaultReaderError

sealed trait EnvReaderError extends AllError
case class EnvNotFound(varName: String) extends EnvReaderError
case class EnvParseError(varName: String, msg: String) extends EnvReaderError

sealed trait ArgReaderError extends AllError
case class ArgNotFound(varName: String) extends ArgReaderError
case class ArgParseError(varName: String, msg: String) extends ArgReaderError
