package rps

import model._
import wiro.annotation._

import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}

import io.buildo.enumero.{CaseEnumIndex, CaseEnumSerialization}
import cats.Monad
import cats.syntax.all._
import cats.effect.Effect
import cats.effect.syntax.effect._

@path("rps")
trait GameController {
  @command
  def play(userMove: Move): Future[Either[Throwable, Result]]

  @query
  def result(id: UUID): Future[Either[Throwable, PlayResponse]]
}

class GameControllerImpl[F[_]: Effect](gameService: GameService[F])(
  implicit ec: ExecutionContext
) extends GameController {

  override def play(userMove: Move): Future[Either[Throwable, Result]] =
    gameService.playMove(userMove).attempt.toIO.unsafeToFuture

  override def result(id: UUID): Future[Either[Throwable, PlayResponse]] =
    gameService
      .getResult(id)
      .map { maybePlay =>
        maybePlay
          .map(play => PlayResponse(play.userMove, play.computerMove, play.result))
          .toRight(new IllegalStateException)
      }
      .toIO
      .unsafeToFuture

}
