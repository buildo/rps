lazy val root = project
  .in(file("."))
  .settings(
    name := "rock-paper-scissor",
    scalaVersion := "2.13.6",
    scalacOptions += "-Ymacro-annotations",
    libraryDependencies ++= List(
      "io.buildo" %% "enumero" % "1.4.2",
      "io.buildo" %% "enumero-circe-support" % "1.4.2",
      "org.flywaydb" % "flyway-core" % "7.5.4",
      "org.postgresql" % "postgresql" % "42.2.5",
      "com.typesafe.slick" %% "slick" % "3.3.2",
      "com.typesafe.slick" %% "slick-hikaricp" % "3.3.2",
      "com.typesafe" % "config" % "1.4.1",
      "org.slf4j" % "slf4j-nop" % "1.6.4"
    )
  )
