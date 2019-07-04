package rps
import scala.io.StdIn.readLine
import scala.util.Random
object Main extends App {
  Game.play
}

object Game {
  def play() : Unit = {
    val moveMap = Map("0" -> "Rock", "1" -> "Paper", "2" -> "Scissor")
    println("Choose Rock (0), Paper(1), Scissor(2)")
    val move = moveMap.getOrElse(readLine(),"None")
    val computerMove = moveMap(Random.nextInt(3).toString)
    val resultString = s"$move VS $computerMove -> "
    val result =(move, computerMove) match {
      case (x,y) if x == y => resultString + "Draw!"
      case ("Rock","Paper") => resultString  + "You lost!"
      case ("Rock","Scissor") => resultString  + "You win!"
      case ("Paper","Rock") => resultString  + "You win!"
      case ("Paper","Scissor") => resultString  + "You lost!"
      case ("Scissor","Rock") => resultString  + "You lost!"
      case ("Scissor","Paper") => resultString  + "You win!"
      case (_,_) =>  "Not an Option!"
    }
    println(result)
    //play()
  }
}