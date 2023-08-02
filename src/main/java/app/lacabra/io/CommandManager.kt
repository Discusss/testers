package app.lacabra.io

import app.lacabra.interfaces.Command
import app.lacabra.io.Clazz.getCommandsOfClass
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import org.slf4j.LoggerFactory

class CommandManager {

    val commands: MutableMap<String, Command> = mutableMapOf()
    private val logger = LoggerFactory.getLogger(CommandManager::class.java)

    init {
        if (instance != null)
            throw Error("There is already an instance of CommandManager!")
        else
            instance = this

        commands += getCommandsOfClass(Command::class.java).associateBy { it.name }
        logger.info("Registered ${commands.size} commands")
    }

    suspend fun run(event: SlashCommandInteractionEvent) {
        if (!event.isFromGuild) return event.reply("No puedes hacer eso aquí").queue()

        val command = commands[event.name] ?: return event.reply("Ese comando no existe").queue()
        command.hook(event)
    }

    companion object {
        var instance: CommandManager? = null
    }
}