package rps

import io.buildo.enumero.CaseEnumSerialization
import java.util.UUID
import java.sql.Timestamp
import java.time.Instant
import slick.jdbc.JdbcBackend.DatabaseDef
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.{ExecutionContext}
import scala.concurrent.Future
import db.Tables.{Match, Row}
import model._

trait GameRepository {
  def save(
      play: Play
  ): Future[UUID]

  def readLastMatch(): Future[Option[Play]]
}

object GameRepository {
  def create(
      db: DatabaseDef
  )(implicit
      ec: ExecutionContext
  ) = new GameRepository {

    val moveSerializer = CaseEnumSerialization[Move]
    val resultSerializer = CaseEnumSerialization[Result]

    def save(
        play: Play
    ): Future[UUID] = {
      val rowId = UUID.randomUUID
      val row = Row(
        id = rowId,
        computerMove = moveSerializer.caseToString(play.computerMove),
        userMove = moveSerializer.caseToString(play.userMove),
        result = resultSerializer.caseToString(play.result),
        occurredAt = Timestamp.from(Instant.now)
      )
      db.run(Match += row).map(_ => rowId)
    }

    def readLastMatch(): Future[Option[Play]] =
      db.run(Match.sortBy(_.occurredAt.desc).take(1).result.headOption)
        .map(_.flatMap(convertPlayRow))

    private val convertPlayRow = (r: Row) =>
      for {
        userMove <- moveSerializer.caseFromString(r.userMove)
        computerMove <- moveSerializer.caseFromString(r.computerMove)
        result <- resultSerializer.caseFromString(r.result)
      } yield Play(userMove, computerMove, result)

  }
}
