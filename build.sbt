ThisBuild / scalaVersion := "2.13.8"
ThisBuild / version := "0.1.0"
ThisBuild / organization := "io.buildo"
ThisBuild / organizationName := "buildo"

lazy val root = (project in file("."))
  .settings(
    name := "rps"
  )
