package app.lacabra.events

import dev.minn.jda.ktx.util.SLF4J
import net.dv8tion.jda.api.sharding.ShardManager

class EventManager(
    shards: ShardManager
) {

    private val logger by SLF4J

    init {
        logger.info("Registering discord events...")
        ShardHandler(shards)
        InteractionHandler(shards)
    }
}