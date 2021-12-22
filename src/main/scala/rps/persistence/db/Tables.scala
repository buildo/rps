package rps.db

import java.sql.Timestamp
import java.util.UUID

import slick.jdbc.PostgresProfile.api._

object Tables {

  case class Row(
      id: UUID,
      cpuMove: String,
      playerMove: String,
      results: String,
      matchDate: Timestamp
  )

  class GameHistory(tag: Tag)
      extends Table[Row](tag, Some("GameHistory"), "history") {

    def id = column[java.util.UUID]("id", O.PrimaryKey)
    def userMove = column[String]("cpu_move")
    def computerMove = column[String]("player_move")
    def results = column[String]("result")
    def matchDate = column[Timestamp]("match_date")

    def * = (
      id,
      userMove,
      computerMove,
      results,
      matchDate
    ) <> (Row.tupled, Row.unapply)

  }
  lazy val Match = TableQuery[GameHistory]
}
