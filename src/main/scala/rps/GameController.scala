package rps

import java.util.UUID

import scala.concurrent.{ExecutionContext, Future}
import wiro.annotation._

import model._

@path("rps")
trait GameController {
  @command
  def play(userMove: Move): Future[Either[Throwable, UUID]]

  @query
  def result(): Future[Either[Throwable, PlayResponse]]
}

class GameControllerImpl(gameService: GameService)(implicit ec: ExecutionContext) extends GameController {
  override def result(): Future[Either[Throwable, PlayResponse]] = {
    gameService.getResult.map(p => p.flatMap(_.toRight(new IllegalStateException).map {
      play => PlayResponse(play.id, play.userMove, play.computerMove, play.result, play.createdAt)
    }))
  }

  override def play(userMove: Move): Future[Either[Throwable, UUID]] = gameService.playMove(userMove)
}
