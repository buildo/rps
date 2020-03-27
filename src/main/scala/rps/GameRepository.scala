package rps

import scala.concurrent.{ExecutionContext, Future}
import java.util.UUID
import java.sql.Timestamp

import slick.driver.H2Driver.backend.DatabaseDef
import slick.driver.H2Driver.api._

import db.Tables.{Plays, PlayRow}
import model._
import Move._
import Result._

trait GameRepository {
  def save(play: Play): Future[Either[Throwable, UUID]]
  def read(): Future[Either[Throwable, Option[Play]]]
}

class GameRepositoryImpl(
  db: DatabaseDef
)(
  implicit ec: ExecutionContext
) extends GameRepository with SlickRepository {

  def save(play: Play): Future[Either[Throwable, UUID]] = {
    val newPlay = Plays += convertPlay(play)
    
    futureToEither(db.run(newPlay).map(_ => play.id))
  }

  def read(): Future[Either[Throwable, Option[Play]]] = { 
    val play = db.run(Plays.sortBy(_.createdAt.desc).take(1).result.headOption).map(_.flatMap(convertPlayRow))

    futureToEither(play)
  } 

  private val convertPlayRow = (r: PlayRow) => for {
    userMove <- Move.caseFromString(r.userMove)
    computerMove <- Move.caseFromString(r.computerMove)
    result <- Result.caseFromString(r.result)
  } yield Play(r.id, userMove, computerMove, result, r.createdAt.toInstant)

  private def convertPlay(game: Play): PlayRow =
    PlayRow(
      game.id,
      Move.caseToString(game.userMove),
      Move.caseToString(game.computerMove),
      Result.caseToString(game.result),
      Timestamp.from(game.createdAt)
    )
}