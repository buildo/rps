package rps
package test

import org.scalatest.{ FlatSpec, FunSpec, Matchers }
import org.scalatest.prop.Checkers

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.testkit.ScalatestRouteTest

import io.circe.generic.auto._
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport._
import io.buildo.enumero.circe._

import org.scalacheck.{ Properties, Gen }
import org.scalacheck.Prop.{ forAll, BooleanOperators }

class RPSSpec extends FlatSpec with ScalatestRouteTest with Matchers {

  val route = WebServer.route

  "when sending an invalid move" should "return a 422 status" in {
    Post("/rps/play", Map("userMove" -> "invalid")) ~> route ~> check {
      status shouldBe UnprocessableEntity
    }
  }

  "when playing" should "return a serialized PlayResult" in {
    Post("/rps/play", WebServer.PlayPayload("Rock")) ~> route ~> check {
      status shouldBe OK
      responseAs[WebServer.PlayResult].userMove shouldBe Move.Rock
    }
  }
}

class RPSProperties extends FunSpec with Checkers {

  val playWinResultGen: Gen[WebServer.PlayResult] = for {
    userMove <- Gen.oneOf("Rock", "Paper", "Scissors")
    result = WebServer.PlayResult.tupled(Game.run(userMove).get)
    if result.result == Result.Win
  } yield result

  val playDrawResultGen: Gen[WebServer.PlayResult] = for {
    userMove <- Gen.oneOf("Rock", "Paper", "Scissors")
    result = WebServer.PlayResult.tupled(Game.run(userMove).get)
    if result.result == Result.Draw
  } yield result

  describe("Game") {
    it("should win only when user move beats computer move") {
      val prop = forAll(playWinResultGen) {
        case WebServer.PlayResult(_, Move.Rock, Move.Scissors) => true
        case WebServer.PlayResult(_, Move.Scissors, Move.Paper) => true
        case WebServer.PlayResult(_, Move.Paper, Move.Rock) => true
        case _ => false
      }
      check(prop)
    }

    it("should draw only when user move equals computer move") {
      val prop = forAll(playDrawResultGen) {
        case WebServer.PlayResult(_, m1, m2) if (m1 == m2) => true
        case _ => false
      }
      check(prop)
    }
  }

}
