lazy val root = project.in(file("."))
  .settings(
    name := "rock-paper-scissor",
    scalaVersion := "2.12.10",
    resolvers += "buildo at bintray" at "https://dl.bintray.com/buildo/maven",
    libraryDependencies += "io.buildo" %% "enumero" % "1.4.0",
    resolvers += Resolver.sonatypeRepo("releases"),
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
  )
