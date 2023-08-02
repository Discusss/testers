package app.lacabra.interfaces

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData

interface Command {

    suspend fun hook(event: SlashCommandInteractionEvent)

    val name: String
    val description: String

    val data: SlashCommandData
}