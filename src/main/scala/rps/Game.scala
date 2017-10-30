package rps

import scala.util.Random
import model.{Move, Result}
import Move._
import Result._

import io.buildo.enumero.{CaseEnumIndex, CaseEnumSerialization}

object Game {
  def play(userMove: Move): (Move, Move, Result) = {
    val computerMove = generateComputerMove()
    val result = (userMove, computerMove) match {
      case (Rock, Scissors) | (Paper, Rock) | (Scissors, Paper) => Win
      case (x, y) if x == y => Draw
      case _ => Lose
    }
    (userMove, computerMove, result)
  }

  private def generateComputerMove(): Move =
    Random.shuffle(List(Rock, Paper, Scissors)).head
}
