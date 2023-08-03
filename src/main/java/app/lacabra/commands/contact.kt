package app.lacabra.commands

import app.lacabra.interfaces.Command
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData

class contact: Command {

    override suspend fun hook(event: SlashCommandInteractionEvent) {
        TODO("Not yet implemented")
    }

    override val name: String = "contact"
    override val description: String = "Contactar con el autor de un reporte"
    override val data: SlashCommandData = Commands.slash(name, description)
        .addOption(OptionType.STRING, "id", "ID del reporte", true)
        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
}