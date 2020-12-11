lazy val root = project.in(file("."))
  .settings(
    name := "rock-paper-scissor",
    scalaVersion := "2.12.3",
    resolvers += Resolver.bintrayRepo("buildo", "maven"),
    libraryDependencies += "io.buildo" %% "enumero" % "1.2.1",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.0" % "test",
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
  )
