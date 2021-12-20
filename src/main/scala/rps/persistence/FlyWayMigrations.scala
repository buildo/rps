package rps

import org.flywaydb.core.Flyway
import scala.concurrent.{ExecutionContext, Future}
import com.typesafe.config.ConfigFactory

final case class Migration(
    state: String,
    version: String,
    description: String
)

trait Migrations {
  def migrate(): Future[Unit]
}

class FlywayMigrations(flyway: Flyway)(implicit ec: ExecutionContext)
    extends Migrations {

  override def migrate(): Future[Unit] = Future {
    flyway.migrate
  }
}

object FlywayMigrations {
  val dbConfig = ConfigFactory.load("application.conf").getConfig("db")
  val flywayConfig =
    ConfigFactory.load("application.conf").getConfig("flyway")

  def create()(implicit ec: ExecutionContext): Future[FlywayMigrations] = {

    Future {
      val flyway = Flyway.configure
        .dataSource(
          dbConfig.getString("url"),
          dbConfig.getString("user"),
          dbConfig.getString("password")
        )
        .schemas(dbConfig.getString("schema"))
        .locations(flywayConfig.getString("path"))
        .load

      new FlywayMigrations(flyway)
    }
  }
}
