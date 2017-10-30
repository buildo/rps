lazy val root = project.in(file("."))
  .settings(
    name := "rock-paper-scissor",
    scalaVersion := "2.12.3",
    resolvers += Resolver.bintrayRepo("buildo", "maven"),
    libraryDependencies ++= Seq(
      "io.buildo" %% "enumero" % "1.2.1",
      "io.buildo" %% "enumero-circe-support" % "1.2.1",
      "com.typesafe.akka" %% "akka-http" % "10.0.10",
      "de.heikoseeberger" %% "akka-http-circe" % "1.18.0",
      "io.circe" %% "circe-core" % "0.8.0",
      "io.circe" %% "circe-generic" % "0.8.0"
    ),
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
  )
