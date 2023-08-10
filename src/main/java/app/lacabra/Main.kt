package app.lacabra

import app.lacabra.config.Config
import app.lacabra.db.Postgres
import app.lacabra.events.EventManager
import app.lacabra.io.CommandManager
import dev.minn.jda.ktx.jdabuilder.injectKTX
import dev.minn.jda.ktx.util.SLF4J
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder
import net.dv8tion.jda.api.sharding.ShardManager
import net.dv8tion.jda.api.utils.Compression
import net.dv8tion.jda.api.utils.messages.MessageRequest
import java.util.EnumSet
import kotlin.system.exitProcess

object Main {

    private lateinit var shards: ShardManager
    private val logger by SLF4J

    @Suppress("unused")
    val commands = CommandManager()
    val config = Config().data

    @JvmStatic
    fun main(args: Array<String>) {

        Postgres().connect()

        // this disables all cache flags and gateway intents
        val builder = DefaultShardManagerBuilder.createLight(null, EnumSet.noneOf(GatewayIntent::class.java))
        MessageRequest.setDefaultMentionRepliedUser(false)

        builder.setBulkDeleteSplittingEnabled(true)
        builder.setCompression(Compression.ZLIB)
        builder.setActivity(Activity.watching("lacabra.app"))
        builder.setToken(config.token)

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