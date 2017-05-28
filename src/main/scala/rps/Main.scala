package rps

import scala.io.StdIn.readLine
import scala.util.Random.shuffle
import scala.concurrent.{ Future, ExecutionContext }
import io.buildo.enumero.annotations.enum
import io.buildo.enumero.{ CaseEnum, CaseEnumSerialization }
import io.buildo.enumero.circe._
import wiro.annotation.{ command, path }
import wiro.server.akkaHttp.{ HttpRPCServer, RouterDerivationModule, FailSupport, ToHttpResponse }
import wiro.server.akkaHttp.RouteGenerators._
import wiro.models.Config
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl.model.{ HttpResponse, StatusCodes, ContentType, HttpEntity }
import akka.http.scaladsl.model.MediaTypes
import akka.http.scaladsl.server.Directives._

import io.circe.generic.auto._

case class PlayResult(result: Result, userMove: RPS.Move, computerMove: RPS.Move)
case class InvalidMove(move: String)

@path("rps")
trait RpsController {
  @command
  def play(userMove: String): Future[Either[InvalidMove, PlayResult]]
}

class RpsControllerImpl(implicit ec: ExecutionContext) extends RpsController {
  @command
  def play(userMove: String): Future[Either[InvalidMove, PlayResult]] =
    RPS.run(userMove) match {
      case Some(result) => Future(Right(PlayResult.tupled(result)))
      case None => Future(Left(InvalidMove(userMove)))
    }
}

object errors {
  import FailSupport._

  import io.circe.syntax._

  implicit val invalidMoveToResponse = new ToHttpResponse[InvalidMove] {
    def response(error: InvalidMove) = HttpResponse(
      status = StatusCodes.UnprocessableEntity,
      entity = HttpEntity(ContentType(MediaTypes.`application/json`), error.asJson.noSpaces)
    )
  }
}

object Main extends App with RouterDerivationModule {
  import errors._
  import FailSupport._
  import wiro.reflect._

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher

  val rpsController = new RpsControllerImpl: RpsController
  implicit val rpsRouter = deriveRouter[RpsController](rpsController)

  val server = new HttpRPCServer(
    config = Config("localhost", 8080),
    controllers = List(rpsController),
    customRoute = options(complete("ok"))
  )
}

@enum trait Result {
  object Win
  object Lose
  object Draw
}

trait Game {
  type Move <: CaseEnum
  def run(move: String): Option[(Result, Move, Move)]
  def generateComputerMove(implicit ces: CaseEnumSerialization[Move]): Move =
    shuffle(ces.values.toList).head
}

object RPS extends Game {
  type Move = RPSMove

  @enum trait RPSMove {
    object Rock
    object Paper
    object Scissors
  }

  import RPSMove._

  implicit class OrderedMove(m1: Move) extends Ordered[Move] {
    def compare(m2: Move): Int = (m1, m2) match {
      case (Rock, Scissors) | (Paper, Rock) | (Scissors, Paper) => 1
      case (m1, m2) if m1 == m2 => 0
      case _ => -1
    }
  }

  def run(move: String): Option[(Result, Move, Move)] = {
    val computerMove = generateComputerMove
    val userMove = toMove(move)
    userMove.map { userMove =>
      val result = (userMove, computerMove) match {
        case (m1, m2) if m1 > m2  => Result.Win
        case (m1, m2) if m1 < m2  => Result.Lose
        case (m1, m2) if m1 == m2 => Result.Draw
      }
      (result, userMove, computerMove)
    }
  }

  private def toMove(s: String): Option[Move] =
    CaseEnumSerialization[Move].caseFromString(s)

}

trait Player {
  def play(game: Game): Unit = {
    import Result._
    val move = readLine("your move> ")
    if (move == "q") {
      println("Bye!")
      sys.exit(0)
    }
    game.run(move) match {
      case None =>
        println("Invalid move. Try again")
        play(game)

      case Some((result, userMove, computerMove)) =>
        result match {
          case Win => println("You win!")
          case Lose => println("You lose!")
          case Draw => println("It's a draw!")
        }
        println(s"User:     $userMove")
        println(s"Computer: $computerMove")
    }
  }

}
