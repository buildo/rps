package rps

import scala.io.StdIn
import scala.util.Random

object Game {
  def play(): Unit = {
    val userMove = StdIn.readLine("Your move (0: Rock, 1: Paper, 2: Scissors)>")
    val computerMove = generateComputerMove()
    println(s"Your move: $userMove. Computer move: $computerMove")
    (userMove, computerMove) match {
      case ("0", "2") | ("1", "0") | ("2", "1") =>
        println("You win!")
      case (x, y) if x == y =>
        println("It's a draw!")
      case _ =>
        println("You lose :(")
    }
  }

  private def generateComputerMove(): String = {
    Random.nextInt(3).toString
  }
}
