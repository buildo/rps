package rps

// object Main extends App with Game {
  // play()
// }

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes._
import akka.stream.ActorMaterializer

import scala.io.StdIn

import io.circe.generic.auto._
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport._
import io.buildo.enumero.circe._

object WebServer extends App {
  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext = system.dispatcher

  case class PlayPayload(userMove: String)
  case class PlayResult(result: Result, userMove: Move, computerMove: Move)
  case class Error(message: String)

  def route =
    pathPrefix("rps") {
      path("play") {
        post {
          entity(as[PlayPayload]) { payload =>
            Game.run(payload.userMove) match {
              case Some((result, userMove, computerMove)) => complete(PlayResult(result, userMove, computerMove))
              case None => complete(UnprocessableEntity, Error(s"${payload.userMove} is an invalid move. Try again!"))
            }
          }
        }
      }
    } ~
    options(complete("ok"))

  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done
}
