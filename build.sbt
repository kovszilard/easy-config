import Dependencies._

ThisBuild / scalaVersion     := "2.13.1"
ThisBuild / crossScalaVersions := List("2.13.1", "2.12.10")
ThisBuild / version          := "0.1.0"
ThisBuild / organization     := "com.github.kovszilard"
ThisBuild / organizationName := "kovszilard"


lazy val root = (project in file("."))
  .settings(
    name := "easy-config",
    libraryDependencies += scalaTest % Test,
    libraryDependencies ++= Seq(
      "com.chuusai" %% "shapeless" % "2.3.3"
    ),
    libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value,
    scalacOptions ++= Seq(
//      "-Xlog-implicits"
      "-deprecation"
    )
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
