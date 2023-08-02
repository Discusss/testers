package app.lacabra

import app.lacabra.events.EventManager
import app.lacabra.io.dotenv
import dev.minn.jda.ktx.jdabuilder.injectKTX
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder
import net.dv8tion.jda.api.sharding.ShardManager
import net.dv8tion.jda.api.utils.Compression
import net.dv8tion.jda.api.utils.messages.MessageRequest
import org.slf4j.LoggerFactory
import java.util.EnumSet
import kotlin.system.exitProcess

object Main {

    lateinit var shards: ShardManager
    private val logger = LoggerFactory.getLogger(Main::class.java)

    @JvmStatic
    fun main(args: Array<String>) {

        // this disables all cache flags and gateway intents
        val builder = DefaultShardManagerBuilder.createLight(null, EnumSet.noneOf(GatewayIntent::class.java))
        MessageRequest.setDefaultMentionRepliedUser(false)

        builder.setBulkDeleteSplittingEnabled(true)
        builder.setCompression(Compression.ZLIB)
        builder.setActivity(Activity.watching("lacabra.app"))
        builder.setToken(dotenv.TOKEN)

        builder.setLargeThreshold(50)

        builder.setShardsTotal(-1)
        builder.injectKTX()

        shards = try {
            builder.build().let {
                EventManager(it)
                it
            }
        } catch (e: Exception) {
            logger.error("Error while starting the bot", e)
            exitProcess(1)
        }
    }

}