package rps.model

import java.util.UUID

import slick.jdbc.H2Profile.api._

object Tables extends Tables

trait Tables {

  case class PlayRow(id: UUID, userMove: String, computerMove: String, result: String)

  class PlayData(tag: Tag) extends Table[PlayRow](tag, "PLAY") {
    def id = column[UUID]("ID", O.PrimaryKey)
    def userMove = column[String]("USER_MOVE")
    def computerMove = column[String]("COMPUTER_MOVE")
    def result = column[String]("RESULT")

    def * = (id, userMove, computerMove, result) <> (PlayRow.tupled, PlayRow.unapply)
  }

  val plays = TableQuery[PlayData]
}
