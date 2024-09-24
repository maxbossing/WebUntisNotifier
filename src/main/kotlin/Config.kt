import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.io.path.*
import kotlin.time.Duration


@Serializable
data class Config(
    val untis: Untis,
    val pushover: Pushover
)

@Serializable
data class Untis(
    val server: String,
    val school: String,
    val username: String,
    val password: String,
    val refreshDelaySeconds: Int = 5 * 60
)

@Serializable
data class Pushover(
    val apiKey: String,
    val groupKey: String,
)

fun loadConfig(): Config? =
    Path("config.json").let {
        if (!it.exists()) {
            if (!it.parent.exists())
                it.parent.createDirectories()
            it.createFile()
        }
        try {
            println("config: ${it.readText()}")
            Json.decodeFromString(it.readText())
        } catch (e: Exception) {
            null
        }
    }
