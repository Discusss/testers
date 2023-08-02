package app.lacabra.io

import io.github.cdimascio.dotenv.Dotenv
import org.slf4j.LoggerFactory
import kotlin.system.exitProcess

object dotenv {

    private val logger = LoggerFactory.getLogger(dotenv::class.java)

    var TOKEN: String
    var POSTGRES_URI: String

    init {
        val dotenv: Dotenv
        try {
            dotenv = Dotenv.configure().ignoreIfMissing().ignoreIfMalformed().load()
        } catch (e: Exception) {
            println("No he encontrado el archivo .env")
            exitProcess(1)
        }

        TOKEN = getEnv("TOKEN", dotenv) ?: run {
            logger.error("No he encontrado la variable TOKEN en el archivo .env")
            exitProcess(1)
        }
        POSTGRES_URI = getEnv("POSTGRES_URI", dotenv) ?: run {
            logger.error("No he encontrado la variable POSTGRES_URI en el archivo .env")
            exitProcess(1)
        }

    }

    private fun getEnv(key: String, dotenv: Dotenv): String? {
        if(System.getenv(key) != null) {
            return System.getenv(key)
        } else if(dotenv.get(key) != null) {
            return dotenv.get(key)
        }
        return null
    }

}