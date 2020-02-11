package easyconfig

sealed abstract class ConfigError(message: String)
case class Help(message: String) extends ConfigError(message)
case class MissingField(message: String) extends ConfigError(message)
