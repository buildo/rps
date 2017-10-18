package rps

import scala.util.Random

object Game {
  def play(): Unit = {
    val userMove = readLine("your move (0: Rock, 1: Paper, 2: Scissors)> ")
    val computerMove = generateComputerMove()
    println(s"Your move: $userMove. Computer move: $computerMove")
    (userMove, computerMove) match {
      case ("0", "2") | ("1", "0") | ("2", "1") => println("You Win!")
      case (x, y) if x == y => println("It's a Draw!")
      case _ => println("You Lose :(")
    }
  }

  private val r = scala.util.Random

  private def generateComputerMove(): String =
    r.nextInt(3).toString
}