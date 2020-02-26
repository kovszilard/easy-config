import Dependencies._

lazy val scala212 = "2.12.10"
lazy val scala213 = "2.13.1"
lazy val supportedScalaVersions = List(scala213, scala212)

ThisBuild / scalaVersion     := scala213
ThisBuild / crossScalaVersions := List("2.13.1", "2.12.10")
ThisBuild / organization     := "com.github.kovszilard"
ThisBuild / organizationName := "kovszilard"

import ReleaseTransformations._
ThisBuild / releaseCrossBuild := true // true if you cross-build the project for multiple Scala versions
ThisBuild / releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  // For non cross-build projects, use releaseStepCommand("publishSigned")
  releaseStepCommandAndRemaining("+publishSigned"),
  releaseStepCommand("sonatypeBundleRelease"),
  setNextVersion,
  commitNextVersion,
  pushChanges
)

useGpgAgent := true
useGpgPinentry := true

lazy val root = (project in file("."))
  .settings(
    name := "easy-config",
    libraryDependencies ++= Seq(scalaTest % Test, shapeless),
    scalacOptions ++= Seq(
//      "-Xlog-implicits"
      "-deprecation"
    )
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
