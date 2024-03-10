package rps

import scala.concurrent.{Future, ExecutionContext}
import scala.concurrent.duration._
import slick.jdbc.JdbcBackend.Database
import zio.Console.{printLine, readLine}
import zio.{Random, UIO, ZIO}

import model.{Move, Play, ReadLastMatchError, Result}
import persistence.GameRepository
import Move.*
import Result.*

object Game {
  given ExecutionContext = ExecutionContext.global

  val database = Database.forConfig("db")
  val gameRepository = GameRepository.create(database)

  def play(): UIO[Unit] =
    for {
      _ <- printLastGameMessage
      maybePlay <- playRound()
      _ <- maybePlay match {
        case None       => ZIO.unit
        case Some(play) => gameRepository.save(play)
      }
    } yield ()

  def playRound(): UIO[Option[Play]] = for {
    rawUserMove <- readLine("Your move (0: Rock, 1: Paper, 2: Scissors)>").orDie
    play <- Move.read(rawUserMove) match {
      case None =>
        printLine(
          "Sorry, you must enter a valid move (0, 1 or 2). Try again"
        ).orDie.map(_ => None)
      case Some(userMove) =>
        for {
          computerMove <- generateComputerMove()
          _ <- printLine(
            s"Your move: ${Move.show(userMove)}. Computer move: ${Move.show(computerMove)}"
          ).orDie
          matchResult = evaluatePlay(userMove, computerMove)
          _ <- printLine(matchResult match {
            case Win  => "You win!"
            case Draw => "It's a draw!"
            case Lose => "You lose :("
          }).orDie
        } yield Some(Play(userMove, computerMove, matchResult))
    }
  } yield play

  def evaluatePlay(userMove: Move, computerMove: Move): Result =
    (userMove, computerMove) match {
      case (Rock, Scissors) | (Paper, Rock) | (Scissors, Paper) => Win
      case (Rock, Rock) | (Paper, Paper) | (Scissors, Scissors) => Draw
      case (Scissors, Rock) | (Rock, Paper) | (Paper, Scissors) => Lose
    }

  private def generateComputerMove(): UIO[Move] =
    Random.shuffle(List(Rock, Paper, Scissors)).map(_.head)

  private def printLastGameMessage: UIO[Unit] =
    gameRepository
      .readLastMatch()
      .flatMap { lastPlay =>
        val result = lastPlay.result match {
          case Win  => "You won"
          case Draw => "Draw"
          case Lose => "You lost"
        }
        val userMove = Move.show(lastPlay.userMove)
        val computerMove = Move.show(lastPlay.computerMove)
        val message =
          s"LAST ROUND: $result (Your move: $userMove. Computer move: $computerMove)"
        printLine(message).orDie
      }
      .catchAll { case ReadLastMatchError.NoLastMatch =>
        printLine("No previous results found").orDie
      }
}
