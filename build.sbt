ThisBuild / scalaVersion := "3.3.1"
ThisBuild / version := "0.1.0"
ThisBuild / organization := "io.buildo"
ThisBuild / organizationName := "buildo"

val V = new {
  val config = "1.4.3"
  val flyway = "10.9.0"
  val munit = "1.0.0-M11"
  val postgresql = "42.7.2"
  val slf4jNop = "2.0.12"
  val slick = "3.5.0"
}

lazy val root = (project in file("."))
  .settings(
    name := "rps",
    libraryDependencies ++= List(
      "com.typesafe" % "config" % V.config,
      "com.typesafe.slick" %% "slick" % V.slick,
      "com.typesafe.slick" %% "slick-hikaricp" % V.slick,
      "org.flywaydb" % "flyway-core" % V.flyway,
      "org.flywaydb" % "flyway-database-postgresql" % V.flyway,
      "org.postgresql" % "postgresql" % V.postgresql,
      "org.slf4j" % "slf4j-nop" % V.slf4jNop,
      "org.scalameta" %% "munit" % V.munit % Test
    )
  )
