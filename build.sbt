ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.12.15"
addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)

val zioVersion    = "2.0.0-RC5"
val circeVersion  = "0.14.1"
val http4sVersion = "0.23.12"
libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value
libraryDependencies ++= Seq(
  "dev.zio" %% "zio",
  "dev.zio" %% "zio-streams",
).map(_ % zioVersion) ++ Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-generic-extras",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion) ++ Seq(
  "org.http4s" %% "http4s-blaze-server",
  "org.http4s" %% "http4s-circe",
  "org.http4s" %% "http4s-dsl"
).map(_ % http4sVersion)
libraryDependencies += "dev.zio" %% "zio-interop-cats" % "3.3.0-RC6"

lazy val root = (project in file("."))
  .settings(
    name := "zio-challenge",
    idePackagePrefix := Some("com.daniel")
  )
