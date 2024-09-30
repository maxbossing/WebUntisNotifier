import config.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import kotlinx.coroutines.*
import notifications.AbstractNotificationProvider
import notifications.impl.NtfyNotificationProvider
import notifications.impl.PushoverNotificationProvider
import store.LessonNotificationStore
import untis.closingUntisSession
import untis.parseChange
import untis.todaysTimetable
import utils.e
import utils.ifTrue
import kotlin.properties.Delegates
import kotlin.time.Duration.Companion.seconds

val ktor by lazy { HttpClient(CIO) }

var debug by Delegates.notNull<Boolean>()
    private set

lateinit var notificationProvider: AbstractNotificationProvider<*>
    private set

suspend fun main() = coroutineScope {
    val config = loadConfig() ?: e("cannot read config")
    debug = config.debug
    notificationProvider = when(config.notifications) {
        is PushoverNotificationConfig -> {
            println("i: initializing Pushover notification provider")
            PushoverNotificationProvider(config.notifications)
        }
        is NtfyNotificationConfig -> {
            println("i: initializing Ntfy notification provider")
            NtfyNotificationProvider(config.notifications)
        }
    }

    launch {
        while (isActive) {
            closingUntisSession(config.untis) { session ->
                val timeTable = session.todaysTimetable().apply { sortByStartTime() }
                for (lesson in timeTable) {
                    (lesson.parseChange() ?: continue)
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