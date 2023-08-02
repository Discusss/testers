package app.lacabra.events

import app.lacabra.Main.config
import app.lacabra.io.CommandManager
import dev.minn.jda.ktx.events.listener
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.sharding.ShardManager

class SlashHandler(shards: ShardManager) {

    init {
        shards.listener<SlashCommandInteractionEvent> { event ->

            val user = event.user
            if (user.isBot) return@listener
            if (event.isAcknowledged) return@listener

            if (!event.isFromGuild || event.guild!!.id != config.guild_id) {
                event.reply("Este bot no puede ser usado fuera del servidor principal").queue()
                return@listener
            }

            CommandManager.instance!!.run(event)
        }
    }

}