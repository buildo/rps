package rps

import java.util.UUID

import slick.jdbc.H2Profile.api._
import io.buildo.enumero.CaseEnumSerialization

import model._

trait GameRepository[F[_]] {
  def setup(): F[Unit]
  def save(play: Play): F[UUID]
  def read(id: UUID): F[Option[Tables.PlayRow]]
}

final class SlickGameRepository extends GameRepository[DBIO] {

  override def setup(): DBIO[Unit] =
    Tables.plays.schema.create

  override def save(play: Play): DBIO[UUID] = {
    val id = UUID.randomUUID
    println(id)
    (Tables.plays += Tables.PlayRow(
      id,
      CaseEnumSerialization[Move].caseToString(play.userMove),
      CaseEnumSerialization[Move].caseToString(play.computerMove),
      CaseEnumSerialization[Result].caseToString(play.result)
    )).andThen(DBIO.successful(id))
  }

  override def read(id: UUID): DBIO[Option[Tables.PlayRow]] =
    Tables.plays
      .filter(_.id === id)
      .result
      .headOption

}
