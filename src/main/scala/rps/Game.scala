package rps

import scala.util.Random
import model.Move
import Move._
import io.buildo.enumero.CaseEnumIndex

object Game {
  def play(): Unit = {
    val rawUserMove = readLine("your move (0: Rock, 1: Paper, 2: Scissors)> ")
    implicitly[CaseEnumIndex[Move]].caseFromIndex(rawUserMove) match {
      case None => println("Sorry, you must enter a valid move (0, 1 or 2). Try again")
      case Some(userMove) => 
        val computerMove = generateComputerMove()
        println(s"Your move: ${userMove}. Computer move: ${computerMove}")
        (userMove, computerMove) match {
          case (Rock, Scissors) | (Paper, Rock) | (Scissors, Paper) => println("You Win!")
          case (x, y) if x == y => println("It's a Draw!")
          case _ => println("You Lose :(")
        }
    }
  }

  private def generateComputerMove(): Move =
    Random.shuffle(List(Rock, Paper, Scissors)).head
}