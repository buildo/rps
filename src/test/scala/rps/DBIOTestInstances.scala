package rps

import scala.concurrent.{ExecutionContext, Future}

import slick.dbio.DBIO
import slick.jdbc.PostgresProfile.api._
import org.scalatest.concurrent.ScalaFutures
import cats.effect.IO

trait DBIOTestInstances extends ScalaFutures {

  protected implicit def executionContext: ExecutionContext
  protected def db: Database

  private case class IntentionalRollbackException[R](result: R)
      extends Exception("Rolling back transaction after test")

  def withRollback[A](testCode: => DBIO[A]): IO[A] = {
    val testWithRollback = testCode.flatMap(a => DBIO.failed(IntentionalRollbackException(a)))

    val testResult = IO.fromFuture(IO(db.run(testWithRollback.transactionally)))

    testResult.handleErrorWith {
      case IntentionalRollbackException(success) => IO.pure(success.asInstanceOf[A])
    }
  }
}
