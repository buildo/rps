package rps

import scala.concurrent.Future
import scala.concurrent.ExecutionContext

trait SlickRepository {
  def futureToEither[R](f: Future[R])(implicit ec: ExecutionContext): Future[Either[Throwable, R]] = {
    f.map { Right(_) }.recover { case e => Left(e) }
  }    
}