package rps

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import scala.io.StdIn

import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport._
import io.circe.generic.auto._
import io.buildo.enumero.circe._

import rps.model.{PlayRequest, PlayResponse}

object Main extends App {
  implicit val system = ActorSystem("rps")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val route =
    pathPrefix("rps") {
      path("play") {
        post {
          entity(as[PlayRequest]) { apiMove =>
            val playResponse = PlayResponse.tupled(Game.play(apiMove.userMove))
            complete(playResponse)
          }
        }
      }
    } ~ options(complete())

  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done
}
