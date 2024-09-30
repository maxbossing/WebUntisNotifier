package config

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import utils.createIfNotExists
import utils.i
import kotlin.io.path.*


private val json = Json {
    classDiscriminator = "type"
}

@Serializable
data class Config(
    val debug: Boolean = false,
    val untis: Untis,
    val notifications: NotificationConfig
)



fun loadConfig(): Config? =
    Path("config.json").let {
        it.createIfNotExists()
        try {
            println("config: ${it.readText()}")
            json.decodeFromString(it.readText())
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
