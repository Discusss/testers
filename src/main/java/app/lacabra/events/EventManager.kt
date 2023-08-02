package app.lacabra.events

import net.dv8tion.jda.api.sharding.ShardManager
import org.slf4j.LoggerFactory

class EventManager(
    shards: ShardManager
) {

    private val logger = LoggerFactory.getLogger(EventManager::class.java)

    init {
        logger.info("Registering discord events...")
        ShardHandler(shards)
        SlashHandler(shards)
    }
}