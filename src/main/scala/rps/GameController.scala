package rps

import java.util.UUID
import scala.concurrent.Future
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

  private[this] def resultZIO: IO[RPSError, PlayResponse] =
    gameService
      .getResult()
      .someOrFail(RPSError.NeverPlayed)
      .map(play => PlayResponse(play.userMove, play.computerMove, play.result))


  private[this] def playZIO(userMove: Move): IO[RPSError, UUID] =
    gameService.playMove(userMove)

  override def result(): Future[Either[RPSError, PlayResponse]] =
    runtime.unsafeRunToFuture(resultZIO.either)

  override def play(userMove: Move): Future[Either[RPSError, UUID]] =
    runtime.unsafeRunToFuture(playZIO(userMove).either)
}
