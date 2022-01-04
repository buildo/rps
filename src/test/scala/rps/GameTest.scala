package test

import rps.Game.evaluatePlay
import rps.model.Move
import rps.model.Move._
import rps.model.Result._
import rps.model.Result

class GameTest extends munit.FunSuite {

  case class Play(userMove: Move, computerMove: Move)

  def check(
      name: String,
      inputs: List[Play],
      expected: Result
  )(implicit loc: munit.Location): Unit = {
    test(name) {
        inputs.map(play => evaluatePlay(play.userMove, play.computerMove))
        .foreach(assertEquals(_, expected))
    }
  }

  check(
    "player win",
    List(Play(Paper, Rock), Play(Rock, Scissors), Play(Scissors, Paper)),
    PlayerWin
  )

  check(
    "computer win",
    List(
      Play(Rock, Paper),
      Play(Scissors, Rock),
      Play(Paper, Scissors)
    ),
    CPUWin
  )

  check(
    "draw",
    List(Play(Paper, Paper), Play(Rock, Rock), Play(Scissors, Scissors)),
    Draw
  )
}
