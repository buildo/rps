name := "rps"
scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  "io.buildo" %% "enumero" % "1.1.0",
  "io.buildo" %% "enumero-circe-support" % "1.1.0",
  "com.typesafe.akka" %% "akka-http" % "10.0.7",
  "de.heikoseeberger" %% "akka-http-circe" % "1.16.0"
)

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic"
).map(_ % "0.8.0")

libraryDependencies ++= Seq(
  "org.scalacheck" %% "scalacheck" % "1.13.5",
  "org.scalatest" %% "scalatest" % "3.0.1",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.0.2"
).map(_ % Test)



addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
resolvers += Resolver.bintrayRepo("buildo", "maven")
