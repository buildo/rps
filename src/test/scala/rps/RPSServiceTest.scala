package rps

import scala.collection.mutable.Map
import java.util.UUID

import slick.jdbc.PostgresProfile.api._
import org.scalatest._
import org.scalatest.prop.PropertyChecks
import org.scalacheck.magnolia._
import cats.Id
import cats.~>
import cats.effect.IO

import model._

class RPSServiceTest extends FunSpec with OptionValues with Matchers with PropertyChecks {

  val repository = new GameRepository[Id] {

    val repo: Map[UUID, Play] = Map.empty

    override def setup = ()

    override def save(play: Play): Id[UUID] = {
      val id = UUID.randomUUID
      repo += ((id, play))
      id
    }

    override def read(id: UUID): Id[Option[Play]] =
      repo.get(id)
  }

  implicit val trans = new (Id ~> IO) {
    override def apply[A](fa: Id[A]): IO[A] = IO.pure(fa)
  }

  val service = new GameServiceImpl[IO, Id](repository)

  it("should save a play") {
    forAll { userMove: Move =>
      service.playMove(userMove)

      repository.repo.values.find(p => p.userMove == userMove) shouldBe defined
    }
  }
}
