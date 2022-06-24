package rps.model

sealed trait Result

object Result {
  case object Win extends Result
  case object Lose extends Result
  case object Draw extends Result
}
