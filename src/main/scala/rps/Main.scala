package rps

import scala.util.{Success, Failure}
import scala.concurrent.{ExecutionContext, Future, Await}
import scala.concurrent.duration._
import scala.language.postfixOps

object Main extends App {

  implicit val ec: ExecutionContext = ExecutionContext.global

  private def applyDBMigrations(): Future[Unit] = for {
    flywayMigrations <- FlywayMigrations.create
    result <- flywayMigrations.migrate
  } yield result

  val attemptMigration = applyDBMigrations
    .transformWith {
      case Success(result) => Future(println("Migration complete"))
      case Failure(exception) =>
        Future(println(s"Something went wrong, ${exception.getMessage}"))
    }

  Await.result(attemptMigration, 3 second)
  Game.play()

}
