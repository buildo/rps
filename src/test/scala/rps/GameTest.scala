package rps.test

import rps.Game.evaluatePlay
import rps.model.{Move, Result}
import Move._
import Result._

class GameTest extends munit.FunSuite {

  case class Play(userMove: Move, computerMove: Move)

  def check(
      name: String,
      inputs: List[Play],
      expected: Result
  )(using loc: munit.Location): Unit = {
    test(name) {
      inputs
        .map(play => evaluatePlay(play.userMove, play.computerMove))
        .foreach(assertEquals(_, expected))
    }
  }

  check(
    "player win",
    List(Play(Paper, Rock), Play(Rock, Scissors), Play(Scissors, Paper)),
    Win
  )

  check(
    "computer win",
    List(
      Play(Rock, Paper),
      Play(Scissors, Rock),
      Play(Paper, Scissors)
    ),
    Lose
  )

  check(
    "draw",
    List(Play(Paper, Paper), Play(Rock, Rock), Play(Scissors, Scissors)),
    Draw
  )
}
