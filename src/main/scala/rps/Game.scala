package rps

import scala.io.StdIn
import scala.util.Random

object Game {
  def play(): Unit = {
    val userMove =
      StdIn.readLine("Your move (0: Rock, 1: Paper, 2: Scissors)> ")
    val computerMove = generateComputerMove()
    println(s"Your move: $userMove. Computer move: $computerMove")
    if (userMove == computerMove) {
      println("It's a draw!")
    } else if (
      userMove == "0" && computerMove == "2" ||
      userMove == "2" && computerMove == "1" ||
      userMove == "1" && computerMove == "0"
    ) {
      println("You win!")
    } else {
      println("You lose :(")
    }
  }

  private val r = scala.util.Random

  private def generateComputerMove(): String =
    r.nextInt(3).toString
}
