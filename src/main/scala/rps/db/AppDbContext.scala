package rps.db

// Use H2Driver to connect to an H2 database
import slick.driver.H2Driver.api._
import slick.driver.H2Driver.backend.DatabaseDef
import scala.concurrent.Future
import scala.concurrent.ExecutionContext

object AppDbContext {

  def getDBRef(config_key: String)(implicit ec: ExecutionContext): DatabaseDef = 
    Database.forConfig(config_key)  

  def createSchema(db: DatabaseDef): Future[Unit] =
    db.run((Tables.Plays.schema).create)
}