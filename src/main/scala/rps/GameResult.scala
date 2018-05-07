package rps

trait GameResult

class Victory(winner: String) extends GameResult{
    override def toString = {
        "%s won".format(winner)
    }
}

class Parity extends GameResult{
    override def toString = {
        "parity"
    }
}