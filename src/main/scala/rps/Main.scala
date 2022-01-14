package rps

import scala.util.{Success, Failure}
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration
import scala.language.postfixOps

object Main extends App {
  implicit val ec: ExecutionContext = ExecutionContext.global

  FlywayMigrations.applyMigrations
  Await.result(Game.play(), Duration.Inf)
}
