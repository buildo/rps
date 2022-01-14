package rps.db

import java.sql.Timestamp
import java.util.UUID
import slick.jdbc.PostgresProfile.api._

object Tables {

  case class Row(
      id: UUID,
      computerMove: String,
      userMove: String,
      result: String,
      occurredAt: Timestamp
  )

  class GameHistory(tag: Tag)
      extends Table[Row](tag, Some("rps"), "history") {

    def id = column[java.util.UUID]("id", O.PrimaryKey)
    def computerMove = column[String]("computer_move")
    def userMove = column[String]("user_move")
    def result = column[String]("result")
    def occurredAt = column[Timestamp]("occurred_at")

    def * = (
      id,
      computerMove,
      userMove,
      result,
      occurredAt
    ) <> (Row.tupled, Row.unapply)

  }
  lazy val Match = TableQuery[GameHistory]
}
