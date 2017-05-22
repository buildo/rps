name := "rock-paper-scissor"
scalaVersion := "2.12.2"
libraryDependencies += "io.buildo" %% "enumero" % "1.1.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % Test
libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.13.5" % Test
addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

