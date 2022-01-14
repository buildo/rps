package rps

import scala.io.StdIn
import scala.util.Random
import model.Move
import Move._

object Game {
  def play(): Unit = {
    val rawUserMove =
      StdIn.readLine("Your move (0: Rock, 1: Paper, 2: Scissors)> ")
    Move.read(rawUserMove) match {
      case None =>
        println("Sorry, you must enter a valid move (0, 1 or 2). Try again")
      case Some(userMove) =>
        val computerMove = generateComputerMove()
        println(
          s"Your move: ${Move.show(userMove)}. Computer move: ${Move.show(computerMove)}"
        )
        (userMove, computerMove) match {
          case (Rock, Scissors) | (Paper, Rock) | (Scissors, Paper) =>
            println("You win!")
          case (x, y) if x == y => println("It's a draw!")
          case _                => println("You lose :(")
        }
    }
  }

  private def generateComputerMove(): Move =
    Random.shuffle(List(Rock, Paper, Scissors)).head
}
