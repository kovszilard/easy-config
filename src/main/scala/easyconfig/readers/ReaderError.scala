package easyconfig.readers

sealed trait ReaderError

sealed trait NotFoundError{
  val fieldName: String
}
sealed trait ParseError{
  val fieldName: String
}

sealed trait DefaultReaderError extends ReaderError
case class DefaultNotFound(fieldName: String) extends DefaultReaderError with NotFoundError

sealed trait EnvReaderError extends ReaderError
case class EnvNotFound(fieldName: String, varName: String) extends EnvReaderError with NotFoundError
case class EnvParseError(fieldName: String, varName: String, msg: String) extends EnvReaderError with ParseError

sealed trait ArgReaderError extends ReaderError
case class ArgNotFound(fieldName: String, varName: String) extends ArgReaderError with NotFoundError
case class ArgParseError(fieldName: String, varName: String, msg: String) extends ArgReaderError with ParseError
