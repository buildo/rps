package rps

import scala.concurrent.ExecutionContext
import java.util.UUID
import java.sql.Timestamp
import java.time.Instant

import slick.driver.H2Driver.backend.DatabaseDef
import slick.driver.H2Driver.api._
import db.Tables.{PlayRow, Plays}
import model._
import zio.{IO, Task}

trait GameRepository {
  def save(play: Play): IO[RPSError, UUID]

  def read(): IO[RPSError, Option[Play]]
}

class GameRepositoryImpl(
  db: DatabaseDef
)(
  implicit ec: ExecutionContext
) extends GameRepository {

  override def save(play: Play): IO[RPSError, UUID] = {
    val playRow = convertPlay(play)
    val newPlay = Plays += playRow

    IO.fromFuture { _ => db.run(newPlay) }
      .as(playRow.id)
      .mapError(e => RPSError.DBError(e.getMessage))
  }

  override def read(): IO[RPSError, Option[Play]] = {
    val selectPlay = Plays.sortBy(_.createdAt.desc).take(1).result.headOption
    val maybePlayRowIO: Task[Option[PlayRow]] = IO.fromFuture { implicit ec => db.run(selectPlay) }
    maybePlayRowIO.map(_.flatMap(convertPlayRow))
  }.mapError(e => RPSError.DBError(e.getMessage))

  private def convertPlayRow(r: PlayRow): Option[Play] =
    for {
      userMove <- Move.caseFromString(r.userMove)
      computerMove <- Move.caseFromString(r.computerMove)
      result <- Result.caseFromString(r.result)
    } yield Play(userMove, computerMove, result)

  private def convertPlay(game: Play): PlayRow =
    PlayRow(
      UUID.randomUUID,
      Move.caseToString(game.userMove),
      Move.caseToString(game.computerMove),
      Result.caseToString(game.result),
      Timestamp.from(Instant.now)
    )
}
