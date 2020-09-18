package rps

import org.scalatest.flatspec.AnyFlatSpec
import rps.model.Move._
import rps.model.Result._
import org.scalatest._
import matchers._

class GameTest extends AnyFlatSpec with should.Matchers {

  behavior of "Game"

  it should "win" in {
    Game.play(Rock, Scissors) should be(Win)
    Game.play(Paper, Rock) should be(Win)
    Game.play(Scissors, Paper) should be(Win)
  }

  it should "lose" in {
    Game.play(Rock, Paper) should be(Lose)
    Game.play(Paper, Scissors) should be(Lose)
    Game.play(Scissors, Rock) should be(Lose)
  }

  it should "draw" in {
    Game.play(Rock, Rock) should be(Draw)
    Game.play(Paper, Paper) should be(Draw)
    Game.play(Scissors, Scissors) should be(Draw)
  }

}
