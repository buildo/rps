package rps

import scala.concurrent.{ExecutionContext, Future}
import java.util.UUID
import java.sql.Timestamp
import java.time.Instant

import slick.driver.H2Driver.backend.DatabaseDef
import slick.driver.H2Driver.api._

import db.Tables.{Plays, PlayRow}
import model._

trait GameRepository {
  def save(play: Play): Future[Either[Throwable, UUID]]
  def read(): Future[Either[Throwable, Option[Play]]]
}

class GameRepositoryImpl(
  db: DatabaseDef
)(
  implicit ec: ExecutionContext
) extends GameRepository with SlickRepository {

  override def save(play: Play): Future[Either[Throwable, UUID]] = {
    val playRow = convertPlay(play)
    val newPlay = Plays += playRow
    
    futureToEither(db.run(newPlay).map(_ => playRow.id))
  }

  override def read(): Future[Either[Throwable, Option[Play]]] = { 
    val selectPlay = Plays.sortBy(_.createdAt.desc).take(1).result.headOption
    futureToEither(db.run(selectPlay).map(_.flatMap(convertPlayRow)))
  } 

  private val convertPlayRow = (r: PlayRow) => for {
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