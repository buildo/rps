package rps.db

import java.sql.Timestamp
import java.util.UUID

import slick.driver.H2Driver.api._

object Tables {
  case class PlayRow(
    id: UUID,
    userMove: String, 
    computerMove: String, 
    result: String,
    createdAt: Timestamp
  )

  class Play(tag: Tag)
    extends Table[PlayRow](tag, "PLAY") {
  
    def id = column[java.util.UUID]("PLAY_ID", O.PrimaryKey)
    def userMove = column[String]("USER_MOVE")
    def computerMove = column[String]("COMPUTER_MOVE")
    def result = column[String]("RESULT")
    def createdAt = column[Timestamp]("CREATED_AT")
    
    def * = (id, userMove, computerMove, result, createdAt) <> (PlayRow.tupled, PlayRow.unapply)
  }
  
  lazy val Plays = new TableQuery(tag => new Play(tag))
}