package app.lacabra.io

import app.lacabra.interfaces.Command
import io.github.classgraph.ClassGraph
import org.slf4j.LoggerFactory

object Clazz {

    private const val COMMANDS_PACKAGE = "app.lacabra.commands"
    private val logger = LoggerFactory.getLogger(Clazz::class.java)

    fun getCommandsOfClass(clazz: Class<out Command>): List<Command> {
        val list = mutableListOf<Command>()

        ClassGraph().acceptPackages(COMMANDS_PACKAGE).scan().use { sr ->
            sr.getClassesImplementing(clazz.name).loadClasses().forEach { cmd ->
                runCatching {
                    list.add(cmd.getDeclaredConstructor().newInstance() as Command)
                }.onFailure { logger.error("An error occurred while registering a command!", it) }
            }
        }

        if (list.isEmpty())
            logger.warn("No slash commands were found in package $COMMANDS_PACKAGE")

        return list
    }
}