import io.ktor.client.*
import io.ktor.client.engine.cio.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bytedream.untis4j.UntisUtils
import java.time.LocalDate
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

val ktor by lazy { HttpClient(CIO) }

suspend fun main() = coroutineScope {
    val config = loadConfig() ?: error("cannot read config")
    config.also { (untis: Untis, pushover: Pushover) ->

        launch(Dispatchers.IO) {
            delay(24.hours)
            cancelledLessonCache.clear()
        }

        launch(Dispatchers.IO) {
            while (true) {
                closingUntisSession(untis) { session ->
                    session.getTimetableFromPersonId(
                        LocalDate.now(),
                        LocalDate.now(),
                        session.infos.personId
                    ).apply { sortByStartTime() }
                        .let { timetable ->
                            (0..<timetable.size).forEach { i ->
                                timetable[i].let lesson@{ lesson ->
                                    if (lesson.code == UntisUtils.LessonCode.CANCELLED) {
                                        if (!lesson.messageHasGoneOut()) {
                                            sendCancelledLesson(pushover, i + 1, lesson)
                                            lesson.messageSent()
                                        }
                                    }
                                }
                            }
                        }
                }
                delay(5.minutes)
            }
        }
    }
    Unit
}