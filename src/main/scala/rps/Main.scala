package rps

import scala.io.StdIn.readLine
import scala.util.Random.shuffle
import io.buildo.enumero.annotations.enum

import GameInstances._

object Main extends App with Application {
  play[GameKind.RPS.type]
}

@enum trait GameKind {
  object RPS
  object RPSLS
}

@enum trait Result {
  object Win
  object Lose
  object Draw
}

@enum trait Move {
  object Rock
  object Paper
  object Scissor
}

trait Application {

  def play[A <: GameKind](implicit gameRunner: GameRunner[A]): Unit = {
    val move = readLine("your move> ")
    gameRunner.run(move).map {
      case Result.Win => println("you win!")
      case Result.Lose => println("you lose!")
      case Result.Draw => println("it's a draw!")
    }.getOrElse {
      println(s"$move is an invalid move. Try again!")
      play[A]
    }
  }

}

trait GameRunner[A <: GameKind] {
  def run(move: String): Option[Result]
}

object GameInstances {

  implicit val rpsRunner: GameRunner[GameKind.RPS.type] = new GameRunner[GameKind.RPS.type] {

    override def run(move: String): Option[Result] = {
      toMove(move).map { userMove =>
        (generateComputerMove(), userMove) match {
          case (m1, m2) if m1 == m2 => Result.Draw
          case (Move.Rock, Move.Scissor) => Result.Win
          case (Move.Scissor, Move.Paper) => Result.Win
          case (Move.Paper, Move.Rock) => Result.Win
          case _ => Result.Lose
        }
      }
    }

    def toMove(move: String): Option[Move] = move match {
      case "1" => Some(Move.Rock)
      case "2" => Some(Move.Paper)
      case "3" => Some(Move.Scissor)
      case _   => None
    }

    def generateComputerMove(): Move =
      shuffle(List(Move.Rock, Move.Paper, Move.Scissor)).head

  }
}
