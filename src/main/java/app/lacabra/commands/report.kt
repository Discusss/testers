package app.lacabra.commands

import app.lacabra.Main.config
import app.lacabra.interfaces.Command
import app.lacabra.lib.BugType
import app.lacabra.lib.ErrorType
import app.lacabra.lib.MissingType
import dev.minn.jda.ktx.interactions.components.TextInput
import dev.minn.jda.ktx.interactions.components.getOption
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle
import net.dv8tion.jda.api.interactions.modals.Modal

class report: Command {
    override suspend fun hook(event: SlashCommandInteractionEvent) {

        val allowed = config.testers.contains(event.user.id) || config.admins.contains(event.user.id) || event.member!!.roles.any { it.id in config.admins || it.id in config.testers }
        if (!allowed)
            return event.reply("No tienes permiso para usar este comando").queue()

        val subcommand = event.subcommandName
        when (subcommand) {
            "bug" -> {

                val type = BugType.entries.firstOrNull { it.code == event.getOption<String>("tipo")!! } ?: return event.reply("Invalid report type").queue()

                val modal = Modal.create("report:bug", "Reportar un bug ${type.placeholder}")

                when (type) {
                    BugType.COMMAND -> modal.addActionRow(TextInput("command", "Comando", TextInputStyle.SHORT, true))
                    BugType.EVENT -> modal.addActionRow(TextInput("event", "Evento", TextInputStyle.SHORT, true))
                    BugType.CONFIG -> modal.addActionRow(TextInput("módulo", "Módulo de la configuración", TextInputStyle.SHORT, true))
                    else -> {}
                }

                modal.addActionRow(TextInput("description", "Descripción del bug", TextInputStyle.PARAGRAPH, true))
                event.replyModal(modal.build()).queue()
            }
            "error" -> {

                val type = ErrorType.entries.firstOrNull { it.code == event.getOption<String>("tipo")!! } ?: return event.reply("Invalid report type").queue()

                val modal = Modal.create("report:error", "Reportar un error ${type.placeholder}")

                when (type) {
                    ErrorType.COMMAND -> modal.addActionRow(TextInput("command", "Comando", TextInputStyle.SHORT, true))
                    ErrorType.EVENT -> modal.addActionRow(TextInput("event", "Evento", TextInputStyle.SHORT, true))
                    ErrorType.CONFIG -> modal.addActionRow(TextInput("módulo", "Módulo de la configuración", TextInputStyle.SHORT, true))
                    else -> {}
                }

                modal.addActionRow(TextInput("description", "Descripción del error", TextInputStyle.PARAGRAPH, true))
                event.replyModal(modal.build()).queue()

            }
            "sugerencia" -> {

                val modal = Modal.create("report:suggestion", "Hacer una sugerencia")
                    .addActionRow(TextInput("description", "Descripción de la sugerencia", TextInputStyle.PARAGRAPH, true))
                event.replyModal(modal.build()).queue()

            }
            "falta" -> {

                val type = MissingType.entries.firstOrNull { it.code == event.getOption<String>("tipo")!! } ?: return event.reply("Invalid report type").queue()

                val modal = Modal.create("report:missing", "Falta algo ${type.placeholder}")

                when (type) {
                    MissingType.COMMAND -> modal.addActionRow(TextInput("command", "Comando", TextInputStyle.SHORT, true))
                    MissingType.EVENT -> modal.addActionRow(TextInput("event", "Evento", TextInputStyle.SHORT, true))
                    MissingType.CONFIG -> modal.addActionRow(TextInput("módulo", "Módulo de la configuración", TextInputStyle.SHORT, true))
                    else -> {}
                }

                modal.addActionRow(TextInput("description", "Descripción de lo que falta", TextInputStyle.PARAGRAPH, true))
                event.replyModal(modal.build()).queue()

            }
            else -> {

                val modal = Modal.create("report:other", "Hacer otro tipo de reporte")
                    .addActionRow(TextInput("description", "Descripción del reporte", TextInputStyle.PARAGRAPH, true))
                event.replyModal(modal.build()).queue()

            }
        }
    }

    override val name: String = "report"
    override val description: String = "Reporta un bug o un error de LA CABRA"
    override val data: SlashCommandData = Commands.slash(name, description)
        .addSubcommands(
            SubcommandData("bug", "Reporta un bug")
                .addOptions(
                    OptionData(OptionType.STRING, "tipo", "¿Donde se ha dado este bug?", true)
                        .addChoice("Relacionado con Discord", "discord")
                        .addChoice("en un comando de moderación", "moderation")
                        .addChoice("en un comando", "command")
                        .addChoice("en un evento", "event")
                        .addChoice("en la configuración", "config")
                        .addChoice("en las apelaciones", "appeal")
                        .addChoice("en el sistema de infracciones", "infractions")
                        .addChoice("otro bug", "other")
                ),
            SubcommandData("error", "Reporta un error")
                .addOptions(
                    OptionData(OptionType.STRING, "tipo", "¿Que tipo de error?", true)
                        .addChoice("Relacionado con Discord", "discord")
                        .addChoice("de traducción/ortografía", "translation")
                        .addChoice("en un comando", "command")
                        .addChoice("en un evento", "event")
                        .addChoice("en la configuración", "config")
                        .addChoice("en las apelaciones", "appeal")
                        .addChoice("en el sistema de infracciones", "infractions")
                        .addChoice("otro error", "other")
                ),
            SubcommandData("sugerencia", "Envía una sugerencia"),
            SubcommandData("falta", "Falta algo que debería estar")
                .addOptions(
                    OptionData(OptionType.STRING, "tipo", "¿Donde falta?", true)
                        .addChoice("en un comando", "command")
                        .addChoice("en un evento", "event")
                        .addChoice("en la configuración", "config")
                        .addChoice("en otro sitio", "other")
                ),
            SubcommandData("otro", "Envía un reporte de otro tipo")
        )
}