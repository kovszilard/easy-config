# Easy Config
[![Maven central](https://img.shields.io/maven-central/v/com.github.kovszilard/easy-config_2.12)](https://search.maven.org/search?q=com.github.kovszilard.easy-config)
[![Tweet](https://img.shields.io/twitter/url?style=social&url=https%3A%2F%2Fgithub.com%2Fkovszilard%2Feasy-config)](https://twitter.com/intent/tweet?text=Wow:&url=https%3A%2F%2Fgithub.com%2Fkovszilard%2Feasy-config)
[![Twitter follow](https://img.shields.io/twitter/follow/kovszilard?style=social)](https://twitter.com/intent/follow?screen_name=kovszilard)

Easy Config makes Scala (12 factor) application configuration extremely easy. It reads configuration from the environment or command line arguments.

## Features

* You define a case class for your configuration, and Easy Config gives you an instance. That's it.
* Reads default value from the case class if environment variable or command line argument is not present.
* Environment variables can be overridden by command line arguments.
* Can parse most common types, including Options and Lists.
* Returns help message for command line arguments `-h` and `--help`.
* Returns help message if the configuration is not present or malformed.
* Nested configurations are not supported yet.


## Usage

Include it in your project by adding the following to your build.sbt:

```scala
libraryDependencies += "com.github.kovszilard" %% "easy-config" % "0.1.0"
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

## Do you like this project? ❤️

Please give it a star. It is just one click for you, and it keeps me motivated to write open source tools like this.
