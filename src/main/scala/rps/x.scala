package rps

import scala.io.StdIn.readLine
import scala.util.Random.shuffle
import io.buildo.enumero.annotations.enum
import io.buildo.enumero.CaseEnumSerialization

object Main extends App with Game {
  play()
}

@enum trait Move {
  Rock
  Paper
  Scissors
}

@enum trait Result {
  Win
  Tie
  Lose
}

trait Game {
  def play(): Unit = {
    val move = readLine("you move> ")
    run(move) match {
      case Some((result, userMove, computerMove)) => {
        println(s"""
          |Result:   $result
          |
          |User:     $userMove
          |Computer: $computerMove
          |""".stripMargin
        )

        val response = readLine("Play another game? (y/n)")
        if (response == "y") play()
      }
      case None => {
        println("Invalid move. Try again!")
        play()
      }
    }
  }

  def generateMove(): Move = {
    import Move._
    shuffle(CaseEnumSerialization[Move].values).head
  }

  def toMove(move: String): Option[Move] =
    CaseEnumSerialization[Move].caseFromString(move)

  def run(move: String): Option[(Result, Move, Move)] = {
    import Move._
    val computerMove = generateMove()
    val userMove = toMove(move)

    userMove.map { userMove =>
      val result = (userMove, computerMove) match {
        case (Rock, Scissors) | (Scissors, Paper) | (Paper, Rock) => Result.Win
        case (m1, m2) if (m1 == m2) => Result.Tie
        case _ => Result.Lose
      }
      (result, userMove, computerMove)
    }
  }
}
