package rps

import java.util.UUID
import java.time.Instant

import scala.concurrent.Future
import scala.util.Random

import model._
import Move._
import Result._

trait GameService {
  def playMove(userMove: Move): Future[Either[Throwable, UUID]]
  def getResult(): Future[Either[Throwable, Option[Play]]]
}

class GameServiceImpl(repository: GameRepository) extends GameService {
  override def playMove(userMove: Move): Future[Either[Throwable, UUID]] = {
    val computerMove = generateComputerMove()
    val result = (userMove, computerMove) match {
      case (Rock, Scissors) | (Paper, Rock) | (Scissors, Paper) => Win
      case (x, y) if x == y                                     => Draw
      case _                                                    => Lose
    }    
    val play = Play(UUID.randomUUID, userMove, computerMove, result, Instant.now())

    repository.save(play)
  }

  private def generateComputerMove(): Move =
    Random.shuffle(List(Rock, Paper, Scissors)).head

  override def getResult(): Future[Either[Throwable, Option[Play]]] = 
    repository.read
}