package rps

import scala.concurrent.{Future, ExecutionContext}

trait SlickRepository {

  /* this helper transform generic feature's content to Either Either[Throwable, R]*/
  def futureToEither[R](
      f: Future[R]
  )(implicit ec: ExecutionContext): Future[Either[Throwable, R]] =
    f.map { Right(_) }.recover { case e => Left(e) }

}
