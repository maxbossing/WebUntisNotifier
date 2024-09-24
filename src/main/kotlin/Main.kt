import dev.reformator.stacktracedecoroutinator.common.DecoroutinatorCommonApi
import io.github.aakira.napier.Antilog
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.LogLevel
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import kotlinx.coroutines.*
import org.bytedream.untis4j.UntisUtils
import java.time.LocalDate
import java.util.concurrent.CompletionException
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

val ktor by lazy { HttpClient(CIO) }

suspend fun main() = coroutineScope {
    Napier.base(DebugAntilog())
    val config = loadConfig() ?: error("cannot read config")
    config.also { (untis: Untis, pushover: Pushover) ->

        Napier.i("WebUntisNotifier initialized")

        launch(Dispatchers.Default) {
            delay(24.hours)
            lessonCache.clear()
        }

        launch(Dispatchers.Default) {
            while (isActive) {
                closingUntisSession(untis) { session ->
                    Napier.i("attempting to get timetable information")
                    val timetable = session.getTimetableFromPersonId(
                        LocalDate.now(),
                        LocalDate.now(),
                        session.infos.personId
                    ).apply { sortByStartTime() }

                    val lessons = timetable.lessons()

                    for (lesson in lessons) {
                        Napier.d(lesson.subjects[0].longName)
                        val change = lesson.parseChange() ?: continue
                        if (!cacheLesson(change.first.lessonTime, change.first.type))
                            continue

                        when (change.first) {
                            is LessonCancelledChange -> {
                                Napier.i("Found cancelled Lesson change: time: ${change.first.lessonTime}, name: ${change.first.lessonName}")
                                sendLessonChange(pushover, change.first)
                            }
                            is LessonTeacherChange -> {
                                if (change.second != null) {
                                    if (change.second is LessonRoomChange) {
                                        if (!cacheLesson(change.second!!.lessonTime, change.second!!.type))
                                            continue
                                        Napier.i("Found lesson with changed teacher and room: time: ${change.first.lessonTime}, name: ${change.first.lessonName} ")
                                        sendLessonChange(pushover, change.first, change.second as LessonRoomChange)
                                        continue
                                    }
                                }
                                Napier.i("found lesson with changed teacher: time: ${change.first.lessonTime}, name: ${change.first.lessonName}")
                                sendLessonChange(pushover, change.first)
                            }
                            is LessonRoomChange -> {
                                Napier.i("found lesson with changed room: time: ${change.first.lessonTime}, name: ${change.first.lessonName}")
                                sendLessonChange(pushover, change.first)
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