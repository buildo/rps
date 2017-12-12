package rps

import scala.util.Random
import model.{Move, Result, PlayResponse}
import Move._
import Result._
import wiro.annotation._
import scala.concurrent.{ExecutionContext, Future}

import io.buildo.enumero.{CaseEnumIndex, CaseEnumSerialization}

@path("rps")
trait GameApi {
  @command
  def play(userMove: Move): Future[Either[Throwable, PlayResponse]]
}

class GameApiImpl(implicit ec: ExecutionContext) extends GameApi {
  override def play(userMove: Move): Future[Either[Throwable, PlayResponse]] = Future {
    val computerMove = generateComputerMove()
    val result = (userMove, computerMove) match {
      case (Rock, Scissors) | (Paper, Rock) | (Scissors, Paper) => Win
      case (x, y) if x == y => Draw
      case _ => Lose
    }
    Right(PlayResponse.tupled((userMove, computerMove, result)))
  }

  private def generateComputerMove(): Move =
    Random.shuffle(List(Rock, Paper, Scissors)).head
}
