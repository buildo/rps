name := "rock-paper-scissor"
scalaVersion := "2.12.2"
libraryDependencies += "io.buildo" %% "enumero" % "1.1.0"
libraryDependencies += "io.buildo" %% "enumero-circe-support" % "1.1.0"
libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.0.7"
libraryDependencies += "de.heikoseeberger" %% "akka-http-circe" % "1.16.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % Test
libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.13.5" % Test

val circeVersion = "0.8.0"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic"
).map(_ % circeVersion)

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

