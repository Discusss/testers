package app.lacabra.events

import app.lacabra.io.CommandManager
import dev.minn.jda.ktx.events.listener
import dev.minn.jda.ktx.util.SLF4J
import net.dv8tion.jda.api.events.guild.GuildAvailableEvent
import net.dv8tion.jda.api.events.guild.GuildUnavailableEvent
import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.events.session.ShutdownEvent
import net.dv8tion.jda.api.sharding.ShardManager

class ShardHandler(
    shards: ShardManager
) {

    private val logger by SLF4J

    init {

        shards.listener<ReadyEvent> { event ->
            logger.info("Shard ${event.jda.shardInfo.shardId} is ready")

            if(event.jda.shardInfo.shardId == 0)
                event.jda.updateCommands().addCommands(CommandManager.instance!!.commands.map { it.value.data }).queue()

        }

        shards.listener<ShutdownEvent> { event ->
            logger.info("Shard ${event.jda.shardInfo.shardId} has disconnected")
        }

        shards.listener<GuildAvailableEvent> { event ->
            logger.info("Shard ${event.jda.shardInfo.shardId} has connected to ${event.guild.name}")
        }

        shards.listener<GuildUnavailableEvent> { event ->
            logger.info("Shard ${event.jda.shardInfo.shardId} has disconnected from ${event.guild.name}")
        }
    }
}