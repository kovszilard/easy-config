package easyconfig.readers

sealed trait ReaderError

sealed trait DefaultReaderError extends ReaderError
case class DefaultNotFound(varName: String) extends DefaultReaderError

sealed trait EnvReaderError extends ReaderError
case class EnvNotFound(varName: String) extends EnvReaderError
case class EnvParseError(varName: String, msg: String) extends EnvReaderError

sealed trait ArgReaderError extends ReaderError
case class ArgNotFound(varName: String) extends ArgReaderError
case class ArgParseError(varName: String, msg: String) extends ArgReaderError
