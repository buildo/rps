package rps

import slick._
import slick.jdbc.PostgresProfile.api._
import scala.reflect.ClassTag

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import io.buildo.enumero.{ CaseEnum, CaseEnumSerialization }

import java.sql.Timestamp

object Db {
  implicit def enumeroColumnType[A <: CaseEnum : ClassTag](implicit ces: CaseEnumSerialization[A]) = MappedColumnType.base[A, String](
    { enum => ces.caseToString(enum) },
    { str => ces.caseFromString(str).get }
  )

  case class PlayResultDB(result: Result, userMove: Move, computerMove: Move, createdOn: Timestamp)

  class GameResult(tag: Tag) extends Table[PlayResultDB](tag, "GAME_RESULT") {
    def userMove = column[Move]("USER_MOVE")
    def computerMove = column[Move]("COMPUTER_MOVE")
    def result = column[Result]("RESULT")
    def createdOn = column[Timestamp]("CREATED_ON", O.SqlType("timestamp default now()"))
    def * = (result, userMove, computerMove, createdOn) <> (PlayResultDB.tupled, PlayResultDB.unapply)
  }

  val gameResults = TableQuery[GameResult]

  val db = Database.forConfig("db");
}

object ResultDataModule {
  import Db._

  def createResult(p: WebServer.PlayResult): Future[Int] =
    db.run(gameResults.map(g => (g.result, g.userMove, g.computerMove)) += (p.result, p.userMove, p.computerMove))

  def getResults(): Future[Seq[WebServer.PlayResult]] =
    db.run(gameResults.sortBy(_.createdOn).result).map(gSeq => gSeq.map(g => WebServer.PlayResult(g.result, g.userMove, g.computerMove)))
}
