package rps

import org.scalatest.flatspec.AnyFlatSpec
import rps.model.Move._
import rps.model.Result._
import org.scalatest._
import matchers._

class GameTest extends AnyFlatSpec with should.Matchers {

  behavior of "Game"

  it should "win" in {
    Game.play(Rock, Scissors) should be(Rock, Scissors, Win)
    Game.play(Paper, Rock) should be(Paper, Rock, Win)
    Game.play(Scissors, Paper) should be(Scissors, Paper, Win)
  }

  it should "lose" in {
    Game.play(Rock, Paper) should be(Rock, Paper, Lose)
    Game.play(Paper, Scissors) should be(Paper, Scissors, Lose)
    Game.play(Scissors, Rock) should be(Scissors, Rock, Lose)
  }

  it should "draw" in {
    Game.play(Rock, Rock) should be(Rock, Rock, Draw)
    Game.play(Paper, Paper) should be(Paper, Paper, Draw)
    Game.play(Scissors, Scissors) should be(Scissors, Scissors, Draw)
  }

}
