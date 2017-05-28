package rps

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import scala.io.StdIn

import io.circe.generic.auto._
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport._
import io.buildo.enumero.circe._

object Main extends App {
  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext = system.dispatcher

  case class PlayPayload(userMove: String)
  case class PlayResult(result: Result, userMove: RPS.Move, computerMove: RPS.Move)
  case class Error(message: String)

  val route =
    pathPrefix("rps") {
      path("play") {
        post {
          entity(as[PlayPayload]) { payload =>
            complete {
              RPS.run(payload.userMove) match {
                case Some((result, um, cm)) => PlayResult(result, um, cm)
                case None => Error(s"${payload.userMove} is an invalid move")
              }
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

