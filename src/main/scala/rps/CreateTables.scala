package rps

import slick._
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.Await
import scala.concurrent.duration._

object CreateTables extends App {
  Await.result(Db.db.run(Db.gameResults.schema.create), 5 seconds)
}
