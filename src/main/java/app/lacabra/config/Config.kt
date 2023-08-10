package app.lacabra.config

import app.lacabra.io.ResourceManager
import com.charleskorn.kaml.Yaml
import dev.minn.jda.ktx.util.SLF4J
import java.io.File
import kotlin.system.exitProcess


class Config {

    private val file: File
    private val logger by SLF4J
    var data: Data

    init {

        try {
            file = File("config.yml")
        } catch (e: Exception) {
            logger.error("Failed to load config file", e)
            exitProcess(1)
        }

        if (!file.exists()) {
            logger.error("A new config file has been created. Please fill it with the required information and re-run the bot.")
            createDefaultConfig(file)
            exitProcess(1)
        }

        val content = file.readText()
        val result = Yaml.default.decodeFromString(Data.serializer(), content)
        data = result
        instance = this
    }

    private fun createDefaultConfig(file: File) {
        file.createNewFile()
        val default = ResourceManager("config.yml").content
        file.writeText(default)
    }

    companion object {
        lateinit var instance: Config
    }
}