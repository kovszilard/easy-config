package object easyconfig {

  def easyConfig[A](args: List[String])(implicit configRequest: ConfigRequest[A]): Either[AllError, A] = configRequest.getConfig(args)

}
