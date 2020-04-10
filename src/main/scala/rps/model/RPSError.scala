package rps.model

sealed trait RPSError

object RPSError {

  object NeverPlayed extends RPSError

  case class DBError(message: String) extends RPSError

}
