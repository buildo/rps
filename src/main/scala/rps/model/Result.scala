package rps.model

sealed trait Result
object  Result {
  case object PlayerWin extends Result
  case object CPUWin extends Result
  case object Draw extends Result
}
