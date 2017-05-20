package rps

import scala.io.StdIn.readLine
import scala.util.Random.shuffle
import io.buildo.enumero.annotations.enum
import io.buildo.enumero.CaseEnumSerialization

object Main extends App with Game {
  play()
}

@enum trait Move {
  object Rock
  object Paper
  object Scissor
}

@enum trait Result {
  object Win
  object Lose
  object Draw
}

trait Game {
  def play(): Unit = {
    import Result._
    val move = readLine("your move> ")
    if (move == "q") {
      println("Bye!")
      sys.exit(0)
    }
    run(move) match {
      case None =>
        println("Invalid move. Try again")
        play()

      case Some((result, userMove, computerMove)) =>
        result match {
          case Win => println("You win!")
          case Lose => println("You lose!")
          case Draw => println("It's a draw!")
        }
        println(s"User:     $userMove")
        println(s"Computer: $computerMove")
    }
  }

  private def run(move: String): Option[(Result, Move, Move)] = {
    val computerMove = generateComputerMove
    val userMove = toMove(move)
    import Move._
    userMove.map { userMove =>
      val result = (userMove, computerMove) match {
        case (Rock, Scissor) | (Paper, Rock) | (Scissor, Paper) => Result.Win
        case (m1, m2) if m1 == m2 => Result.Draw
        case _ => Result.Lose
      }
      (result, userMove, computerMove)
    }
  }

  private def toMove(s: String): Option[Move] = s match {
    case "1" => Some(Move.Rock)
    case "2" => Some(Move.Paper)
    case "3" => Some(Move.Scissor)
    case _ => None
  }

  private def generateComputerMove: Move =
    shuffle(CaseEnumSerialization[Move].values.toList).head

}
