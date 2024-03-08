package rps

import scala.util.{Success, Failure}
import scala.concurrent.Await
import scala.concurrent.duration.Duration

import rps.persistence.FlywayMigrations

@main def main(): Unit = {
  FlywayMigrations.applyMigrations
  Await.result(Game.play(), Duration.Inf)
}
