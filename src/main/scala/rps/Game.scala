package rps

import scala.util.Random
import scala.io.StdIn.readLine
import scala.io.AnsiColor._
import scala.concurrent.{Future, ExecutionContext, Await}
import scala.concurrent.duration._
import scala.language.postfixOps
import io.buildo.enumero.{CaseEnumIndex, CaseEnumSerialization}
import model.Move
import Move._
import rps.model.Result
import Result._
import model.Play

object Game {
  implicit val ec = ExecutionContext.global
  val gameRepository = new GameRepositoryImpl(AppDbContext.getDBRef("db"))

  def play(): Unit = {

    for {
      res <- awaitFeature(
        gameRepository.readLastMatch(),
        "impossible to read last match"
      )
      _ <- printLastGameMsg(res)
    } yield ()

    val rawUserMove = readLine("your move (0: Rock, 1: Paper, 2: Scissors)> ")
    CaseEnumIndex[Move].caseFromIndex(rawUserMove) match {
      case None =>
        println("Sorry, you must enter a valid move (0, 1 or 2). Try again")
      case Some(userMove) =>
        val computerMove = generateComputerMove()
        println(
          s"Your move: ${CaseEnumSerialization[Move].caseToString(userMove)}. Computer move: ${CaseEnumSerialization[Move]
            .caseToString(computerMove)}"
        )

        val matchResult = (userMove, computerMove) match {
          case (Rock, Scissors) | (Paper, Rock) | (Scissors, Paper) =>
            println(s"${GREEN}You Win!")
            Result.PlayerWin
          case (x, y) if x == y =>
            println("It's a Draw!")
            Result.Draw
          case _ =>
            println(s"${RED}You Lose :(")
            Result.CPUWin
        }

        for {
          _ <- awaitFeature(
            gameRepository.save(userMove, computerMove, matchResult),
            "impossible to save game result"
          )
        } yield ()
    }
  }

  private def generateComputerMove(): Move =
    Random.shuffle(List(Rock, Paper, Scissors)).head

  private def awaitFeature[R](
      f: Future[Either[Throwable, R]],
      errMsg: String
  ): Option[R] =
    Await
      .result(f, 3 second)
      .fold(
        err => {
          println(
            s"${RED}[ERROR] ${errMsg} : ${err.getMessage}"
          )
          None
        },
        Option.apply
      )

  private def printLastGameMsg(gameMatch: Option[Play]) =
    gameMatch.map(rnd => {
      val message = rnd.result match {
        case Draw =>
          s"CPU [${rnd.computerMove}] -- [${rnd.userMove}] Player ${RESET}"
        case PlayerWin =>
          s"CPU [${rnd.computerMove}] -- ${GREEN}[${rnd.userMove}] Player ${RESET}"
        case CPUWin =>
          s"${RED}CPU [${rnd.computerMove}] ${RESET} -- [${rnd.userMove}] Player"
      }

      println(s"Last round:\n ${message}")
    })
}
