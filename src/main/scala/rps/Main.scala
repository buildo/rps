package rps

import scala.io.StdIn.readLine
import scala.util.Random.shuffle

object Main extends App with Game {
  play()
}

sealed trait Move
object Move {
  case object Rock extends Move
  case object Paper extends Move
  case object Scissor extends Move
}

trait Game {
  def play(): Unit = {
    val move = readLine("your move> ")
    run(move)
  }

  private def run(move: String) = {
    val computerMove = generateComputerMove
    val userMove = toMove(move)
    import Move._
    (userMove, computerMove) match {
      case (Rock, Scissor) => println("win!")
      case (Paper, Rock) => println("win!")
      case (Scissor, Paper) => println("win!")
      case (m1, m2) if m1 == m2 => println("draw!")
      case _ => println("lose")
    }
    println(s"User:     $userMove")
    println(s"Computer: $computerMove")
  }

  private def toMove(s: String): Move = s match {
    case "1" => Move.Rock
    case "2" => Move.Paper
    case "3" => Move.Scissor
  }

  private def generateComputerMove: Move = {
    import Move._
    shuffle(List(Rock, Paper, Scissor)).head
  }

}
