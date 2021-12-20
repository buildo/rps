lazy val persistence = project
  .settings(
    libraryDependencies ++= List(
      "org.flywaydb" % "flyway-core" % "7.5.4",
      "org.postgresql" % "postgresql" % "42.2.5",
      "com.typesafe" % "config" % "1.4.1"
    )
  )

lazy val root = project
  .in(file("."))
  .settings(
    name := "rock-paper-scissor",
    scalaVersion := "2.12.3",
    resolvers += Resolver.bintrayRepo("buildo", "maven"),
    libraryDependencies += "io.buildo" %% "enumero" % "1.2.1",
    addCompilerPlugin(
      "org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full
    )
  )
