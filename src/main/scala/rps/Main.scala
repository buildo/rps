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

import rps.model.PlayResponse

object Main extends App with RouterDerivationModule {
  implicit val system = ActorSystem("rps")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  implicit def throwableResponse: ToHttpResponse[Throwable] = { error =>
    HttpResponse(
      status = StatusCodes.InternalServerError,
      entity = error.toString
    )
  }

  val gameRepository = new InMemoryGameRepository
  val gameService = new GameServiceImpl(gameRepository)
  val gameController = new GameControllerImpl(gameService)
  val gameRouter = deriveRouter[GameController](gameController)

  val rpcServer = new HttpRPCServer(
    config = Config("localhost", 8080),
    routers = List(gameRouter)
  )

}
