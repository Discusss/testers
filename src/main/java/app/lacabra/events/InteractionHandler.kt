package app.lacabra.events

import app.lacabra.Main.config
import app.lacabra.commands.report
import app.lacabra.io.CommandManager
import dev.minn.jda.ktx.events.listener
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.sharding.ShardManager

class InteractionHandler(shards: ShardManager) {

    init {
        shards.listener<SlashCommandInteractionEvent> { event ->

            val user = event.user
            if (user.isBot) return@listener
            if (event.isAcknowledged) return@listener

            if (!event.isFromGuild || event.guild!!.id != config.guild_id) {
                event.reply("Este bot no puede ser usado fuera del servidor principal")
                    .setEphemeral(true)
                    .queue()
                return@listener
            }

            CommandManager.instance!!.run(event)
        }

        shards.listener<ModalInteractionEvent> { event ->
            val args = event.modalId.split(":")
            val command = args[0]

            when (command) {
                "report" -> report.onMenu(event, args.drop(1))
            }
        }
    }

}