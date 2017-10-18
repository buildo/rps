package rps.model

sealed trait Move
object Move {
  case object Rock extends Move
  case object Paper extends Move
  case object Scissors extends Move

  def read(s: String): Option[Move] = s match {
    case "0" => Some(Rock)
    case "1" => Some(Paper)
    case "2" => Some(Scissors)
    case _ => None
  }

  def show(m: Move) = m match {
    case Rock => "🗿"
    case Paper => "📄"
    case Scissors => "✂️"
  }
}