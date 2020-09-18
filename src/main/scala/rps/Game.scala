package rps

import io.buildo.enumero.{CaseEnumIndex, CaseEnumSerialization}
import rps.model.Move._
import rps.model.Result._
import rps.model.{Move, Result}

import scala.io.StdIn.readLine
import scala.util.Random

object Game {
  def play(): Unit = {
    val rawUserMove = readLine("your move (0: Rock, 1: Paper, 2: Scissors)> ")
    CaseEnumIndex[Move].caseFromIndex(rawUserMove) match {
      case None => println("Sorry, you must enter a valid move (0, 1 or 2). Try again")
      case Some(userMove) =>
        val computerMove = generateComputerMove()
        println(s"Your move: ${CaseEnumSerialization[Move].caseToString(userMove)}. Computer move: ${CaseEnumSerialization[Move].caseToString(computerMove)}")
        val result = play(userMove, computerMove)
        result match {
          case Win  => println("You Win!")
          case Draw => println("It's a Draw!")
          case _    => println("You Lose :(")
        }
    }
  }

  def play(userMove: Move, computerMove: Move): Result = {
    (userMove, computerMove) match {
      case (Rock, Scissors) | (Paper, Rock) | (Scissors, Paper) => Win
      case (x, y) if x == y                                     => Draw
      case _                                                    => Lose
    }
  }

  private def generateComputerMove(): Move =
    Random.shuffle(List(Rock, Paper, Scissors)).head
}
