package rps

import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}
import wiro.annotation._
import model._
import zio._

@path("rps")
trait GameController {
  @command
  def play(userMove: Move): Future[Either[RPSError, UUID]]

  @query
  def result(): Future[Either[RPSError, PlayResponse]]
}

class GameControllerImpl(gameService: GameService)(implicit runtime: Runtime[ZEnv])
    extends GameController {

  private[this] def resultZIO: URIO[Any, Either[RPSError, PlayResponse]] =
    gameService
      .getResult()
      .someOrFail(RPSError.NeverPlayed)
      .map(play => PlayResponse(play.userMove, play.computerMove, play.result))
      .either

  private[this] def playZIO(userMove: Move): URIO[Any, Either[RPSError, UUID]] =
    gameService.playMove(userMove).either

  override def result(): Future[Either[RPSError, PlayResponse]] =
    runtime.unsafeRunToFuture(resultZIO)

  override def play(userMove: Move): Future[Either[RPSError, UUID]] =
    runtime.unsafeRunToFuture(playZIO(userMove))
}
