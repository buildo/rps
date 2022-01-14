package rps

import io.buildo.enumero.{CaseEnumIndex, CaseEnumSerialization}
import scala.io.StdIn
import scala.util.Random
import model.{Move, Result}
import Move._
import Result._

object Game {
  def play(): Unit = {
    val rawUserMove =
      StdIn.readLine("Your move (0: Rock, 1: Paper, 2: Scissors)> ")
    CaseEnumIndex[Move].caseFromIndex(rawUserMove) match {
      case None =>
        println("Sorry, you must enter a valid move (0, 1 or 2). Try again")
      case Some(userMove) =>
        val computerMove = generateComputerMove()
        println(
          s"Your move: ${CaseEnumSerialization[Move].caseToString(userMove)}. Computer move: ${CaseEnumSerialization[Move]
            .caseToString(computerMove)}"
        )
        evaluatePlay(userMove, computerMove) match {
          case Win  => println("You win!")
          case Draw => println("It's a draw!")
          case Lose => println("You lose :(")
        }
    }
  }

  def evaluatePlay(userMove: Move, computerMove: Move): Result =
    (userMove, computerMove) match {
      case (Rock, Scissors) | (Paper, Rock) | (Scissors, Paper) => Win
      case (x, y) if x == y                                     => Draw
      case _                                                    => Lose
    }

  private def generateComputerMove(): Move =
    Random.shuffle(List(Rock, Paper, Scissors)).head
}
