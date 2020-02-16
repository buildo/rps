package rps

import java.util.UUID

import slick.jdbc.PostgresProfile.api._
import org.scalatest._
import org.scalatest.prop.PropertyChecks
import org.scalacheck.magnolia._

import model._

class RPSRepositoryTest
    extends FunSpec
    with DBIOTestInstances
    with OptionValues
    with Matchers
    with PropertyChecks {

  implicit val executionContext = scala.concurrent.ExecutionContext.global

  val db = Database.forConfig("h2mem1")
  val repository = new SlickGameRepository

  it("should read a saved play") {
    forAll { play: Play =>
      withRollback {
        for {
          id <- repository.save(play)
          res <- repository.read(id)
        } yield res
      }.map { fromDb =>
        fromDb.value shouldBe play
      }.unsafeRunSync
    }
  }

  it("should return None if the play does not exis") {
    forAll { id: UUID =>
      withRollback {
        for {
          res <- repository.read(id)
        } yield res
      }.map { fromDb =>
        fromDb shouldBe None
      }.unsafeRunSync
    }
  }
}
