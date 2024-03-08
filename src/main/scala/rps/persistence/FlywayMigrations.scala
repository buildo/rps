package rps.persistence

import com.typesafe.config.ConfigFactory
import org.flywaydb.core.Flyway

object FlywayMigrations {
  def applyMigrations: Unit = {
    val dbConfig =
      ConfigFactory.load("reference.conf").getConfig("db")
    val flywayConfig =
      ConfigFactory.load("reference.conf").getConfig("flyway")
    val flyway = Flyway.configure
      .dataSource(
        dbConfig.getString("url"),
        dbConfig.getString("user"),
        dbConfig.getString("password")
      )
      .schemas(dbConfig.getString("schema"))
      .locations(flywayConfig.getString("locations"))
      .load
    flyway.migrate
  }
}
