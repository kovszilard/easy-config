package easyconfig

sealed abstract class ConfigResponse(message: String)
case class Help(message: String) extends ConfigResponse(message)
case class ConfigError(message: String) extends ConfigResponse(message)
