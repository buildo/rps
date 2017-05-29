name := "rps"
scalaVersion := "2.12.2"
libraryDependencies += "io.buildo" %% "enumero" % "1.1.0"
addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
resolvers += Resolver.bintrayRepo("buildo", "maven")
