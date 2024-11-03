import config.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import kotlinx.coroutines.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import notifications.impl.NtfyNotificationProvider
import notifications.impl.PushoverNotificationProvider
import notifications.impl.DiscordNotificationProvider
import store.LessonNotificationStore
import untis.LessonParser
import untis.closingUntisSession
import untis.todaysTimetable
import utils.d
import utils.e
import utils.i
import utils.ifTrue
import kotlin.properties.Delegates
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.seconds

val ktor by lazy { HttpClient(CIO) }

var debug by Delegates.notNull<Boolean>()
    private set

@OptIn(ExperimentalSerializationApi::class)
val json = Json {
    classDiscriminator = "type"
    allowTrailingComma = true
    isLenient = true

}

suspend fun main() = coroutineScope {
    val config = loadConfig() ?: e("cannot read config")
    debug = config.debug
    val notificationProvider = when(config.notifications) {
        is PushoverNotificationConfig -> {
            i("initializing Pushover notification provider")
            PushoverNotificationProvider(config.notifications)
        }
        is NtfyNotificationConfig -> {
            i("initializing Ntfy notification provider")
            NtfyNotificationProvider(config.notifications)
        }
        is DiscordNotificationConfig -> {
            i("initializing Discord notification provider")
            DiscordNotificationProvider(config.notifications)
        }
    }

    val lessonParser = LessonParser(config.timetable)

    launch {
        while(isActive) {
            d("Clearing Lesson store")
            LessonNotificationStore.clear()
            delay(7.days) // I hope this handles errors well enough to ever achieve 7 days uptime
        }
    }

    launch {
        while (isActive) {
            closingUntisSession(config.untis) { session ->
                val timeTable = session.todaysTimetable().apply { sortByStartTime() }
                for (lesson in timeTable) {
                    (lessonParser.parseChange(lesson) ?: continue)
                        .filterNot { LessonNotificationStore.has(it.lessonTime).ifTrue { println("d: non-normal lesson (${it.lessonTime}, ${it.lessonName}) has already been noticed") } }
                        .forEach {
                            LessonNotificationStore.add(it.lessonTime)
                            notificationProvider.sendChanges(it)
                        }
                }
            }
            delay(config.untis.refreshDelaySeconds.seconds)
        }
    }

    Unit
}