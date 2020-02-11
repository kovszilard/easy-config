import easyconfig.helpers.{GenConfig, GenHelp}
import easyconfig.readers.ReaderError

package object easyconfig {

  def easyConfig[A: GenHelp](args: List[String])(implicit configRequest: GenConfig[A]): Either[ConfigError, A] = {

    if (args.contains("-h") || args.contains("--help")) Left(Help(easyConfigHelp[A]))
    else {
      configRequest.getConfig(args) match {
        case Left(_) => Left(MissingField("TODO"))
        case Right(a) => Right(a)
      }
    }
  }

  def easyConfigHelp[A](implicit genHelp: GenHelp[A]): String = genHelp.genHelp

}
