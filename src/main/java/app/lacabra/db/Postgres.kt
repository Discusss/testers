package app.lacabra.db

import app.lacabra.Main.config
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dev.minn.jda.ktx.util.SLF4J
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.system.exitProcess

class Postgres {

    private lateinit var database: Database
    private val logger by SLF4J

    private fun hikariPg(): HikariDataSource {

        val config = HikariConfig().apply {
            driverClassName = "org.postgresql.Driver"
            jdbcUrl = "jdbc:${config.database.host.replace("postgres://", "postgresql://")}"
            username = config.database.username
            password = config.database.password
            addDataSourceProperty("ApplicationName", "lacabra-testers")
            maximumPoolSize = 10
            connectionTimeout = 3000
            idleTimeout = 600000
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        }

        config.validate()
        return HikariDataSource(config)
    }

    fun connect() {
        database = try {
            Database.connect(hikariPg())
        } catch (e: Exception) {
            logger.error("Failed to connect to PostgreSQL database", e)
            exitProcess(1)
        }
        logger.info("Connected to PostgreSQL database")

        transaction {

            SchemaUtils.createMissingTablesAndColumns(

            )
        }
    }

}