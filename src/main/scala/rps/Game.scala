package rps


object Game{
    def play(): Unit = {
        val userMove = chooseShape()
        val random = new scala.util.Random
        var pcMove = Shape.unapply(random.nextInt(2)).getOrElse(throw new RuntimeException("Invalid number"))
        val gameResult = andTheWinnerIs(new ChoiceBundle("Jarvis", pcMove), new ChoiceBundle("you", userMove))
        println("You played %s, Jarvis choose %s, game result: %s".format(userMove, pcMove, gameResult))
    }

    def chooseShape() : Shape = {
        println("Please, choose your shape: (0 for Rock | 1 for Paper | 2 for Scissor)")
        val input = scala.io.StdIn.readLine()
        input match{
            case Shape(input) => input
            case _ => 
                println("Wrong input")
                chooseShape()
        }
    }

    def andTheWinnerIs(p1: ChoiceBundle, p2: ChoiceBundle) : GameResult = {
        if(p1.shape == p2.shape) return new Parity()
        (p1.shape, p2.shape) match{
            case (Rock, Paper) => new Victory(p2.player)
            case (Rock, Scissor) => new Victory(p1.player)
            case (Paper, Rock) => new Victory(p1.player)
            case (Paper, Scissor) => new Victory(p2.player)
            case (Scissor, Paper) => new Victory(p1.player)
            case (Scissor, Rock) => new Victory(p2.player)
            case _ => throw new RuntimeException("ArgumentOufOfRange")
        }
    }
}

