package rps.model

import scala.util.Try

enum Move {
  case Rock
  case Paper
  case Scissors
}

object Move {
  def read(s: String): Option[Move] =
    Try(Move.fromOrdinal(s.toInt)).toOption

  def show(m: Move) = m match {
    case Rock     => "🗿"
    case Paper    => "📄"
    case Scissors => "✂️"
  }
}
