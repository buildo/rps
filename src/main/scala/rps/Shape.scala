package rps

sealed trait Shape

case object Rock extends Shape
case object Paper extends Shape
case object Scissor extends Shape

//extractor for shape class
object Shape {
    def unapply(i: Int) : Option[Shape] = {
        i match{
            case 0 => Some(Rock)
            case 1 => Some(Paper)
            case 2 => Some(Scissor)
            case _ => None
        }
    }   

    def unapply(s: String) : Option[Shape] = try{
        Shape.unapply(s.toInt)
    } catch{
        case _ : Throwable => None
    }
}