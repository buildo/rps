package rps

import scala.io.StdIn.readLine
import scala.util.Random.shuffle
import io.buildo.enumero.annotations.enum
import io.buildo.enumero.{ CaseEnum, CaseEnumSerialization }

object Main extends App with Player {
  play(RPS)
}

@enum trait Result {
  object Win
  object Lose
  object Draw
}

trait Game {
  type Move <: CaseEnum
  def run(move: String): Option[(Result, Move, Move)]
  def generateComputerMove(implicit ces: CaseEnumSerialization[Move]): Move =
    shuffle(ces.values.toList).head
}

object RPS extends Game {
  type Move = RPSMove

  @enum trait RPSMove {
    object Rock
    object Paper
    object Scissor
  }

  import RPSMove._

  implicit class OrderedMove(m1: Move) extends Ordered[Move] {
    def compare(m2: Move): Int = (m1, m2) match {
      case (Rock, Scissor) | (Paper, Rock) | (Scissor, Paper) => 1
      case (m1, m2) if m1 == m2 => 0
      case _ => -1
    }
  }

  def run(move: String): Option[(Result, Move, Move)] = {
    val computerMove = generateComputerMove
    val userMove = toMove(move)
    userMove.map { userMove =>
      val result = (userMove, computerMove) match {
        case (m1, m2) if m1 > m2  => Result.Win
        case (m1, m2) if m1 < m2  => Result.Lose
        case (m1, m2) if m1 == m2 => Result.Draw
      }
      (result, userMove, computerMove)
    }
  }

  private def toMove(s: String): Option[Move] = s match {
    case "1" => Some(Rock)
    case "2" => Some(Paper)
    case "3" => Some(Scissor)
    case _ => None
  }

}

trait Player {
  def play(game: Game): Unit = {
    import Result._
    val move = readLine("your move> ")
    if (move == "q") {
      println("Bye!")
      sys.exit(0)
    }
    game.run(move) match {
      case None =>
        println("Invalid move. Try again")
        play(game)

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

}
