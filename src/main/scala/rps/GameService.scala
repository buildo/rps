package rps

import java.util.UUID
import scala.util.Random

import cats.Monad
import cats.syntax.functor._
import cats.effect.Effect
import cats.~>
import io.buildo.enumero.CaseEnumSerialization

import model._
import Move._
import Result._

trait GameService[F[_]] {
  def playMove(userMove: Move): F[Result]
  def getResult(id: UUID): F[Option[Play]]
}

final class GameServiceImpl[F[_]: Monad, DB[_]: Monad](repository: GameRepository[DB])(
  implicit execute: DB ~> F
) extends GameService[F] {
  override def playMove(userMove: Move): F[Result] = {
    val computerMove = generateComputerMove()
    val result = (userMove, computerMove) match {
      case (Rock, Scissors) | (Paper, Rock) | (Scissors, Paper) => Win
      case (x, y) if x == y                                     => Draw
      case _                                                    => Lose
    }
    val play = Play(userMove, computerMove, result)

    execute.apply {
      for {
        _ <- repository.save(play)
      } yield result
    }
  }

  private def generateComputerMove(): Move =
    Random.shuffle(List(Rock, Paper, Scissors)).head

  override def getResult(id: UUID): F[Option[Play]] =
    execute.apply {
      repository.read(id)
    }
}
