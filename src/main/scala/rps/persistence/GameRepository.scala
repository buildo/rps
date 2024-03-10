package rps.persistence

import java.util.UUID
import java.sql.Timestamp
import java.time.Instant
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.{ExecutionContext}
import scala.concurrent.Future
import zio.{IO, UIO, ZIO}

import rps.model.{Move, Play, ReadLastMatchError, Result}

import Tables.{Match, Row}

trait GameRepository {
  def save(
      play: Play
  ): UIO[UUID]

  def readLastMatch(): IO[ReadLastMatchError, Play]
}

object GameRepository {
  def create(db: Database)(using ec: ExecutionContext) = new GameRepository {

    def save(
        play: Play
    ): UIO[UUID] =
      ZIO.fromFuture { _ =>
        val rowId = UUID.randomUUID
        val row = Row(
          id = rowId,
          computerMove = play.computerMove.toString(),
          userMove = play.userMove.toString(),
          result = play.result.toString(),
          occurredAt = Timestamp.from(Instant.now)
        )
        db.run(Match += row).map(_ => rowId)
      }.orDie

    def readLastMatch(): IO[ReadLastMatchError, Play] =
      ZIO
        .fromFuture { _ =>
          db.run(Match.sortBy(_.occurredAt.desc).take(1).result.headOption)
            .map(_.map { r =>
              Play(
                userMove = Move.valueOf(r.userMove),
                computerMove = Move.valueOf(r.computerMove),
                result = Result.valueOf(r.result)
              )
            })
        }
        .orDie
        .someOrFail(ReadLastMatchError.NoLastMatch)

  }
}
