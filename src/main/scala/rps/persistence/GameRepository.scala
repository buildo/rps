package rps.persistence

import java.util.UUID
import java.sql.Timestamp
import java.time.Instant
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.{ExecutionContext}
import scala.concurrent.Future

import rps.model.{Move, Play, Result}

import Tables.{Match, Row}

trait GameRepository {
  def save(
      play: Play
  ): Future[UUID]

  def readLastMatch(): Future[Option[Play]]
}

object GameRepository {
  def create(db: Database)(using ec: ExecutionContext) = new GameRepository {

    def save(
        play: Play
    ): Future[UUID] = {
      val rowId = UUID.randomUUID
      val row = Row(
        id = rowId,
        computerMove = play.computerMove.toString(),
        userMove = play.userMove.toString(),
        result = play.result.toString(),
        occurredAt = Timestamp.from(Instant.now)
      )
      db.run(Match += row).map(_ => rowId)
    }

    def readLastMatch(): Future[Option[Play]] =
      db.run(Match.sortBy(_.occurredAt.desc).take(1).result.headOption)
        .map(_.map { r =>
          Play(
            userMove = Move.valueOf(r.userMove),
            computerMove = Move.valueOf(r.computerMove),
            result = Result.valueOf(r.result)
          )
        })

  }
}
