package rps

import model._

import java.util.UUID
import java.sql.Timestamp
import slick.jdbc.JdbcBackend.DatabaseDef
import slick.driver.PostgresDriver.api._
import scala.concurrent.{ExecutionContext}
import scala.concurrent.Future
import db.Tables.{Match, Row}
import java.time.Instant
import io.buildo.enumero.CaseEnumSerialization

trait GameRepository {
  def save(
      userMove: Move,
      computerMove: Move,
      result: Result
  ): Future[Either[Throwable, UUID]]

  def readLastMatch(): Future[Either[Throwable, Option[Play]]]
}

class GameRepositoryImpl(
    db: DatabaseDef
)(implicit
    ec: ExecutionContext
) extends GameRepository
    with SlickRepository {

  val moveSerializer = CaseEnumSerialization[Move]
  val playerSerializer = CaseEnumSerialization[Result]

  def save(
      userMove: Move,
      computerMove: Move,
      result: Result
  ): Future[Either[Throwable, UUID]] = {

    val rowId = UUID.randomUUID
    val row = Row(
      rowId,
      moveSerializer.caseToString(computerMove),
      moveSerializer.caseToString(userMove),
      playerSerializer.caseToString(result),
      Timestamp.from(Instant.now)
    )

    futureToEither(db.run(Match += row).map(_ => rowId))
  }

  def readLastMatch(): Future[Either[Throwable, Option[Play]]] =
    futureToEither(
      db.run(Match.sortBy(_.matchDate.desc).take(1).result.headOption)
        .map(_.flatMap(convertPlayRow))
    )

  private val convertPlayRow = (r: Row) =>
    for {
      userMove <- moveSerializer.caseFromString(r.playerMove)
      computerMove <- moveSerializer.caseFromString(r.cpuMove)
      result <- playerSerializer.caseFromString(r.results)
    } yield Play(userMove, computerMove, result)

}
