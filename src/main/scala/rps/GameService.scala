package rps

import java.util.UUID

import rps.model._
import zio.IO
import scala.util.Random
import model._
import Move._
import Result._

trait GameService {
  def playMove(userMove: Move): IO[Throwable, UUID]
  def getResult(): IO[Throwable, Option[Play]]
}

class GameServiceImpl(repository: GameRepository) extends GameService {
  override def playMove(userMove: Move): IO[Throwable, UUID] = {
    val computerMove = generateComputerMove()
    val result = (userMove, computerMove) match {
      case (Rock, Scissors) | (Paper, Rock) | (Scissors, Paper) => Win
      case (x, y) if x == y                                     => Draw
      case _                                                    => Lose
    }    
    val play = Play(userMove, computerMove, result)

    repository.save(play)
  }

  private def generateComputerMove(): Move =
    Random.shuffle(List(Rock, Paper, Scissors)).head

  override def getResult(): IO[Throwable, Option[Play]] =
    repository.read()
}