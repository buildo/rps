package rps

import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}
import wiro.annotation._
import model._
import zio._

@path("rps")
trait GameController {
  @command
  def play(userMove: Move): Future[Either[Throwable, UUID]]

  @query
  def result(): Future[Either[Throwable, PlayResponse]]
}

class GameControllerImpl(gameService: GameService)(implicit ec: ExecutionContext) extends GameController {
  val runtime = Runtime.default

  private[this] def resultZIO: URIO[Any, Either[Throwable, PlayResponse]] = gameService
    .getResult()
    .someOrFail(new IllegalStateException)
    .map(play => PlayResponse(play.userMove, play.computerMove, play.result))
    .either

  private[this] def playZIO(userMove: Move): URIO[Any, Either[Throwable, UUID]] = gameService.playMove(userMove).either

  override def result(): Future[Either[Throwable, PlayResponse]] = runtime.unsafeRunToFuture(resultZIO)

  override def play(userMove: Move): Future[Either[Throwable, UUID]] = runtime.unsafeRunToFuture(playZIO(userMove))
}
