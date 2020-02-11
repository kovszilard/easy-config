import easyconfig.helpers.{GenConfig, GenHelp}
import easyconfig.readers.{ArgParseError, DefaultNotFound, EnvParseError, ParseError, ReaderError}

package object easyconfig {

  def easyConfig[A: GenHelp](args: List[String])(implicit configRequest: GenConfig[A]): Either[String, A] = {

    if (args.contains("-h") || args.contains("--help")) Left(easyConfigHelp[A])
    else {
      configRequest.getConfig(args) match {
        case Left(err) => Left(reportError(err).mkString("\n"))
        case Right(a) => Right(a)
      }
    }
  }

  def easyConfigHelp[A](implicit genHelp: GenHelp[A]): String = genHelp.genHelp

  def reportError(res: List[ReaderError]): List[String] = res.map{
    case DefaultNotFound(fieldName) => s"Configuration $fieldName is missing, please provide default value in code,\n" +
      s" or environment variable ${readers.EnvReader.fieldNameToEnvVar(fieldName)},\n" +
      s" or command line argument ${readers.ArgReader.fieldNameToArg(fieldName)}"
    case EnvParseError(_, varName, msg) => s"Can't parse $varName. $msg"
    case ArgParseError(_, varName, msg) => s"Can't parse $varName. $msg"
    case _ => "Unexpected error, this should never happen..."
  }

}
