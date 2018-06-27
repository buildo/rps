package rps

import scala.collection.concurrent.TrieMap

import model._

trait GameRepository {
  def save(play: Play): Unit
  def read(): Option[Play]
}

class InMemoryGameRepository extends GameRepository {
  private val map = TrieMap.empty[String, Play]

  override def save(play: Play): Unit = {
    map.put("play", play)
    ()
  }

  override def read(): Option[Play] =
    map.get("play")

}
