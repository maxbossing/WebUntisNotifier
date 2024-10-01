package config

import json
import kotlinx.serialization.Serializable
import utils.createIfNotExists
import kotlin.io.path.*

@Serializable
data class Config(
    val debug: Boolean = false,
    val untis: Untis,
    val timetable: TimeTableConfig,
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
