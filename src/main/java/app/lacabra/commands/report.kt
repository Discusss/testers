package app.lacabra.commands

import app.lacabra.Main.config
import app.lacabra.config.Config
import app.lacabra.interfaces.Command
import app.lacabra.lib.BugType
import app.lacabra.lib.ErrorType
import app.lacabra.lib.MissingType
import dev.minn.jda.ktx.interactions.components.TextInput
import dev.minn.jda.ktx.interactions.components.getOption
import dev.minn.jda.ktx.messages.Embed
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle
import net.dv8tion.jda.api.interactions.modals.Modal
import java.time.Instant

class report: Command {

    override suspend fun hook(event: SlashCommandInteractionEvent) {

        val allowed = config.testers.contains(event.user.id) || config.admins.contains(event.user.id) || event.member!!.roles.any { it.id in config.admins || it.id in config.testers }
        if (!allowed)
            return event.reply("No tienes permiso para usar este comando").queue()

        val subcommand = event.subcommandName
        when (subcommand) {
            "bug" -> {

                val type = BugType.entries.firstOrNull { it.code == event.getOption<String>("tipo")!! } ?: return event.reply("Invalid report type").queue()

                val modal = Modal.create("report:bug:${type.name}", "Reportar un bug ${type.placeholder}")

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

                val modal = Modal.create("report:error:${type.name}", "Reportar un error ${type.placeholder}")

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

                val modal = Modal.create("report:missing:${type.name}", "Falta algo ${type.placeholder}")

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

    companion object {

        suspend fun onMenu(event: ModalInteractionEvent, args: List<String>) {

            val category = CATEGORIES.valueOf(args[0].uppercase())
            val type = if (args.getOrNull(1) == null) null else args[1]

            val embed = Embed {
                title = "Nueva incidencia abierta "
                author {
                    name = "Categoría: ${category.name.lowercase()} ${if (type != null) "($type)" else ""}"
                    iconUrl = event.user.effectiveAvatarUrl
                }
                description = "${event.user.asMention} ha abierto una nueva incidencia de la categoría **${category.name.lowercase()} ${if (type != null) "($type)" else ""}**"

                field("ID asignada", "0", true) // TODO: ADD DATABASE

                when (category) {
                    CATEGORIES.BUG -> {

                        val bugType = BugType.entries.firstOrNull { it.name == type } ?: return event.reply("Invalid bug type").setEphemeral(true).queue()
                        color = bugType.color
                        title += bugType.placeholder

                        when (bugType) {
                            BugType.COMMAND -> {
                                val command = event.getValue("command")?.asString ?: return event.reply("Invalid command").setEphemeral(true).queue()
                                field("Nombre del comando", command, true)
                            }
                            BugType.EVENT -> {
                                val eventInput = event.getValue("event")?.asString ?: return event.reply("Invalid event").setEphemeral(true).queue()
                                field("Nombre del evento", eventInput, true)
                            }
                            BugType.CONFIG -> {
                                val module = event.getValue("módulo")?.asString ?: return event.reply("Invalid module").setEphemeral(true).queue()
                                field("Módulo de la configuración", module, true)
                            }
                            else -> {}
                        }

                    }
                    CATEGORIES.ERROR -> {

                        val errorType = ErrorType.entries.firstOrNull { it.name == type } ?: return event.reply("Invalid error type").setEphemeral(true).queue()
                        color = errorType.color
                        title += errorType.placeholder

                        when (errorType) {
                            ErrorType.COMMAND -> {
                                val command = event.getValue("command")?.asString ?: return event.reply("Invalid command").setEphemeral(true).queue()
                                field("Nombre del comando", command, true)
                            }
                            ErrorType.EVENT -> {
                                val eventInput = event.getValue("event")?.asString ?: return event.reply("Invalid event").setEphemeral(true).queue()
                                field("Nombre del evento", eventInput, true)
                            }
                            ErrorType.CONFIG -> {
                                val module = event.getValue("módulo")?.asString ?: return event.reply("Invalid module").setEphemeral(true).queue()
                                field("Módulo de la configuración", module, true)
                            }
                            else -> {}
                        }

                    }
                    CATEGORIES.MISSING -> {

                        val missingType = MissingType.entries.firstOrNull { it.name == type } ?: return event.reply("Invalid missing type").setEphemeral(true).queue()
                        color = missingType.color
                        title += missingType.placeholder

                        when (missingType) {
                            MissingType.COMMAND -> {
                                val command = event.getValue("command")?.asString ?: return event.reply("Invalid command").setEphemeral(true).queue()
                                field("Nombre del comando", command, true)
                            }
                            MissingType.EVENT -> {
                                val eventInput = event.getValue("event")?.asString ?: return event.reply("Invalid event").setEphemeral(true).queue()
                                field("Nombre del evento", eventInput, true)
                            }
                            MissingType.CONFIG -> {
                                val module = event.getValue("módulo")?.asString ?: return event.reply("Invalid module").setEphemeral(true).queue()
                                field("Módulo de la configuración", module, true)
                            }
                            else -> {}
                        }

                    }
                    else -> {}
                }

                field("Descripción", "```${event.getValue("description")?.asString ?: return event.reply("Invalid description").setEphemeral(true).queue()}```", false)
                timestamp = Instant.now()
                footer {
                    name = "Beta testers de LA CABRA"
                }
            }

            event.reply("Gracias por tu reporte! El equipo de LA CABRA lo revisará lo antes posible :)").setEphemeral(true).queue()

            val channel = Config.instance.data.reports.channel_id.let { event.guild?.getTextChannelById(it) } ?: return event.reply("No hay canal de reportes configurado").setEphemeral(true).queue()
            if (Config.instance.data.reports.role_mention?.isNotEmpty() == true)
                channel.sendMessage("<&${Config.instance.data.reports.role_mention}>").addEmbeds(embed).queue()
            else
                channel.sendMessageEmbeds(embed).queue()
        }

        enum class CATEGORIES {
            BUG, ERROR, SUGGESTION, MISSING, OTHER
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