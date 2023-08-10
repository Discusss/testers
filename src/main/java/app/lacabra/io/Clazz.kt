package app.lacabra.io

import app.lacabra.interfaces.Command
import dev.minn.jda.ktx.util.SLF4J
import io.github.classgraph.ClassGraph

object Clazz {

    private const val COMMANDS_PACKAGE = "app.lacabra.commands"
    private val logger by SLF4J

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