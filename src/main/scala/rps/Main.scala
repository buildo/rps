package rps

import scala.io.StdIn.readLine

object Main extends App with Game {
  play()
}

trait Game {
  def play(): Unit = {
    val move = readLine("your move> ")
    val result = run(move)
    ???
  }

  private def run(move: String) = {
    val computerMove = ???
    val userMove = ???
    val result = ???
    result
  }

}
