ThisBuild / scalaVersion := "3.3.1"
ThisBuild / version := "0.1.0"
ThisBuild / organization := "io.buildo"
ThisBuild / organizationName := "buildo"

lazy val root = (project in file("."))
  .settings(
    name := "rps"
  )
