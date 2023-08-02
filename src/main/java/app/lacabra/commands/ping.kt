package app.lacabra.commands

import app.lacabra.interfaces.Command
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData

class ping: Command {
    override suspend fun hook(event: SlashCommandInteractionEvent) {
        event.reply(":ping_pong:  pong!").queue()
    }

    override val name: String = "ping"
    override val description: String = "Pong!"
    override val data: SlashCommandData = Commands.slash(name, description)


}