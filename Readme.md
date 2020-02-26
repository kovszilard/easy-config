# Easy Config
[![Build Status](https://travis-ci.org/kovszilard/easy-config.svg?branch=master)](https://travis-ci.org/kovszilard/easy-config)
[![Maven central](https://img.shields.io/maven-central/v/com.github.kovszilard/easy-config_2.12)](https://search.maven.org/search?q=com.github.kovszilard.easy-config)
[![Tweet](https://img.shields.io/twitter/url?style=social&url=https%3A%2F%2Fgithub.com%2Fkovszilard%2Feasy-config)](https://twitter.com/intent/tweet?text=Wow:&url=https%3A%2F%2Fgithub.com%2Fkovszilard%2Feasy-config)
[![Twitter follow](https://img.shields.io/twitter/follow/kovszilard?style=social)](https://twitter.com/intent/follow?screen_name=kovszilard)

Easy Config makes Scala application configuration extremely easy. It reads configuration from the environment or command line arguments.

## Features

* You define a case class for your configuration, and Easy Config gives you an instance. That's it.
* Reads default value from the case class if environment variable or command line argument is not present.
* Environment variables can be overridden by command line arguments.
* Can parse most common types, including Options and Lists.
* Has a `Secret` data type for storing secret values. It prevents accidental printing or logging of the secret value.
* Returns help message for command line arguments `-h` and `--help`.
* Returns help message if the configuration is not present or malformed.
* Nested configurations are not supported yet.


## Usage

Include it in your project by adding the following to your build.sbt:

```scala
libraryDependencies += "com.github.kovszilard" %% "easy-config" % "0.2.1"
```

Request configuration or a help String if something goes wrong.

```scala
import easyconfig._

object MyApp extends App {

  case class Config(foo: Int, bar: String, baz: Option[List[Int]] = None)

  val config = easyConfig[Config](args)

  config.fold(
    help => println(help),
    config => //do something with the configuration
      println(s"Configuration: $config")
  )

}
```
## Configuration precedence

Case class default values < Environment variables < Command line arguments

Default values defined on the case class are overridden by environment variables. In addition, command line arguments override configuration provided in the environment.

## Creating parsers

In case you have a not supported data type or you want to parse differently than Easy Config does it, then you have to create a parser for yourself. Luckily it is very easy, just define an implicit Parser instance using the `createParser` method. `createParser` takes a function from `String` to `Try[A]`, where `A` is your data type.

See an examples below:

```scala
import scala.util.Try
import easyconfig._
import easyconfig.Parser
import easyconfig.Parser.createParser

object CustomParser extends App {

  case class Name(first: String, last: String)

  implicit val nameParser: Parser[Name] = createParser{ str =>
    Try{
      val fullname = str.split(" ")
      Name(fullname(0), fullname(1))
    }
  }

  case class Config(name: Name)

  val config = easyConfig[Config](args)

  config.fold(
    help => println(help),
    config => //do something with the configuration
      println(s"Configuration: $config\n")
  )
}
```

## Using `Secret`

```scala
import easyconfig._

object Secret extends App {

  case class Config(password: Secret)

  val config = easyConfig[Config](args)

  config.fold(
    help => println(help),
    config =>
      println(s"Printing or logging the configuration won't reveal the secret: $config\n" +
              s"Secret can be revealed by explicitly calling secretValue: ${config.password.secretValue}")
  )

}

```

## Do you like this project? ❤️

Please give it a star. It is just one click for you and it keeps me motivated to write open source tools like this.
