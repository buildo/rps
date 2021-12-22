package rps

import scala.concurrent.ExecutionContext
import slick.jdbc.JdbcBackend.{Database, DatabaseDef}

/* Utility for DB Context */
object AppDbContext {

  def getDBRef(config_key: String)(implicit ec: ExecutionContext): DatabaseDef =
    Database.forConfig(config_key)

}
