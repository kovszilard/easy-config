package example

import easyconfig._

object Secret extends App {

  case class Config(password: Secret)

  val config =easyConfig[Config](args)

  config.fold(
    help => println(help),
    config => //do something with the configuration
      println(s"Printing or logging the configuration won't reveal the secret: $config\n" +
              s"Secret can be revealed by explicitly calling secretValue: ${config.password.secretValue}")
  )

}
