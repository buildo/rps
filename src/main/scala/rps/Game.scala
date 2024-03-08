package rps

import scala.concurrent.{Future, ExecutionContext}
import scala.concurrent.duration._
import scala.io.StdIn
import scala.util.Random
import slick.jdbc.JdbcBackend.Database

import model.{Move, Play, Result}
import persistence.GameRepository
import Move.*
import Result.*

object Game {
  given ExecutionContext = ExecutionContext.global

  val database = Database.forConfig("db")
  val gameRepository = GameRepository.create(database)

  def play(): Future[Unit] =
    for {
      _ <- printLastGameMessage
      maybePlay = playRound()
      _ <- maybePlay match {
        case None       => Future.successful(())
        case Some(play) => gameRepository.save(play)
      }
    } yield ()

  def playRound(): Option[Play] = {
    val rawUserMove =
      StdIn.readLine("Your move (0: Rock, 1: Paper, 2: Scissors)>")
    Move.read(rawUserMove) match {
      case None =>
        println("Sorry, you must enter a valid move (0, 1 or 2). Try again")
        None
      case Some(userMove) =>
        val computerMove = generateComputerMove()
        println(
          s"Your move: ${Move.show(userMove)}. Computer move: ${Move.show(computerMove)}"
        )
        val matchResult = evaluatePlay(userMove, computerMove)
        matchResult match {
          case Win  => println("You win!")
          case Draw => println("It's a draw!")
          case Lose => println("You lose :(")
        }
        Some(Play(userMove, computerMove, matchResult))
    }
  }

  def evaluatePlay(userMove: Move, computerMove: Move): Result =
    (userMove, computerMove) match {
      case (Rock, Scissors) | (Paper, Rock) | (Scissors, Paper) => Win
      case (Rock, Rock) | (Paper, Paper) | (Scissors, Scissors) => Draw
      case (Scissors, Rock) | (Rock, Paper) | (Paper, Scissors) => Lose
    }

  private def generateComputerMove(): Move = {
    Random.shuffle(List(Rock, Paper, Scissors)).head
  }

  private def printLastGameMessage: Future[Unit] =
    gameRepository.readLastMatch().map {
      case None =>
        println("No previous results found")
      case Some(lastPlay) =>
        val result = lastPlay.result match {
          case Win  => "You won"
          case Draw => "Draw"
          case Lose => "You lost"
        }
        val userMove = Move.show(lastPlay.userMove)
        val computerMove = Move.show(lastPlay.computerMove)
        val message =
          s"LAST ROUND: $result (Your move: $userMove. Computer move: $computerMove)"
        println(message)
    }
}
