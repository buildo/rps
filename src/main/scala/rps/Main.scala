package rps

import scala.util.{Success, Failure}
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import zio.{ZIO, ZIOAppDefault}

import rps.persistence.FlywayMigrations

object Main extends ZIOAppDefault {
  def run = for {
    _ <- ZIO.attempt(FlywayMigrations.applyMigrations)
    _ <- Game.play()
  } yield ()
}
