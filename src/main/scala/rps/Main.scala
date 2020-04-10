package rps

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl.model.{ContentType, HttpEntity, HttpResponse, MediaTypes, StatusCodes}
import wiro.Config
import wiro.server.akkaHttp._
import wiro.server.akkaHttp.FailSupport._
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport._
import io.circe.generic.auto._
import io.buildo.enumero.circe._
import rps.model.RPSError
import rps.db.AppDbContext
import zio.Runtime

object Main extends App with RouterDerivationModule {
  implicit val system = ActorSystem("rps")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  implicit def throwableResponse: ToHttpResponse[RPSError] = {
    case RPSError.NeverPlayed =>
      HttpResponse(
        status = StatusCodes.NotFound
      )
    case RPSError.DBError(message) =>
      HttpResponse(
        status = StatusCodes.InternalServerError,
        entity = message
      )
  }

  val db = AppDbContext.getDBRef("h2mem1")
  implicit val runtime = Runtime.default

  AppDbContext
    .createSchema(db)
    .map(_ => {
      val gameRepository = new GameRepositoryImpl(db)
      val gameService = new GameServiceImpl(gameRepository)
      val gameController = new GameControllerImpl(gameService)
      val gameRouter = deriveRouter[GameController](gameController)

      new HttpRPCServer(
        config = Config("localhost", 8080),
        routers = List(gameRouter)
      )
    })
}
