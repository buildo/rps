package rps

import scala.concurrent.ExecutionContext

import cats.Monad
import cats.~>
import cats.effect.IO

import slick.dbio.DBIO
import slick.jdbc.PostgresProfile.api._

trait DBIOInstances {
  implicit def instance(implicit ec: ExecutionContext): Monad[DBIO] = new Monad[DBIO] {
    override def flatMap[A, B](fa: DBIO[A])(f: A => DBIO[B]) = fa.flatMap(f)
    override def pure[A](a: A): DBIO[A] = DBIO.successful(a)
    override def tailRecM[A, B](a: A)(f: A => DBIO[Either[A, B]]): DBIO[B] =
      f(a).flatMap {
        case Left(a1) => tailRecM(a1)(f)
        case Right(b) => DBIO.successful(b)
      }
  }

  def dbioTransformation(db: Database): DBIO ~> IO =
    new (DBIO ~> IO) {
      override def apply[A](fa: DBIO[A]): IO[A] = IO.fromFuture {
        IO(db.run(fa.transactionally))
      }
    }
}

object dbio extends DBIOInstances
