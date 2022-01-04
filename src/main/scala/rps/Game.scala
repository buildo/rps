package rps

import scala.io.StdIn.readLine
import scala.util.Random
import model.Move
import model.Result
import Move._
import Result._

object Game {
  def play(): Unit = {
    val rawUserMove = readLine("your move (0: Rock, 1: Paper, 2: Scissors)> ")
    Move.read(rawUserMove) match {
      case None =>
        println("Sorry, you must enter a valid move (0, 1 or 2). Try again")
      case Some(userMove) =>
        val computerMove = generateComputerMove()
        println(
          s"Your move: ${Move.show(userMove)}. Computer move: ${Move.show(computerMove)}"
        )
        evaluatePlay(userMove, computerMove) match {
          case PlayerWin => println("You Win!")
          case Draw      => println("It's a Draw!")
          case CPUWin    => println("You Lose :(")
        }
    }
  }

  def evaluatePlay(userMove: Move, computerMove: Move): Result =
    (userMove, computerMove) match {
      case (Rock, Scissors) | (Paper, Rock) | (Scissors, Paper) => PlayerWin
      case (x, y) if x == y                                     => Draw
      case _                                                    => CPUWin
    }

  private def generateComputerMove(): Move =
    Random.shuffle(List(Rock, Paper, Scissors)).head
}
