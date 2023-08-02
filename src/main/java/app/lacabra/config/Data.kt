@file:Suppress("PropertyName")

package app.lacabra.config

import kotlinx.serialization.Serializable

@Serializable
data class Data (
    val token: String,
    val guild_id: String,
    val database: Database,
    val admins: List<String>,
    val testers: List<String>
)

@Serializable
data class Database(
    val host: String,
    val username: String,
    val password: String
)