ThisBuild / scalaVersion := "2.13.8"
ThisBuild / version := "0.1.0"
ThisBuild / organization := "io.buildo"
ThisBuild / organizationName := "buildo"

val V = new {
  val enumero = "1.4.2"
  val slick = "3.3.3"
}

lazy val root = (project in file("."))
  .settings(
    name := "rps",
    libraryDependencies ++= List(
      "io.buildo" %% "enumero" % V.enumero,
      "io.buildo" %% "enumero-circe-support" % V.enumero,
      "com.typesafe" % "config" % "1.4.1",
      "com.typesafe.slick" %% "slick" % V.slick,
      "com.typesafe.slick" %% "slick-hikaricp" % V.slick,
      "org.flywaydb" % "flyway-core" % "7.5.4",
      "org.postgresql" % "postgresql" % "42.2.5",
      "org.slf4j" % "slf4j-nop" % "1.6.4"
    ),
    scalacOptions += "-Ymacro-annotations"
  )
