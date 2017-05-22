package rps
package test

import org.scalatest.FunSpec

import org.scalacheck.{ Properties, Gen }
import org.scalacheck.Prop.forAll
import org.scalatest.prop.Checkers

class RPSSpec extends FunSpec with Checkers {

  import RPS._

  val runGen: Gen[(Result, Move, Move)] = for {
    userMove <- Gen.oneOf("1", "2", "3")
  } yield RPS.run(userMove).get

  describe("RPS Game") {
    it("should win only when user move beats computer move") {
      val x = forAll(runGen) { case (result, userMove, computerMove) =>
        result match {
          case Result.Win => userMove > computerMove
          case Result.Lose => computerMove > userMove
          case Result.Draw => userMove == computerMove
        }
      }
      check(x)
    }
  }

}
