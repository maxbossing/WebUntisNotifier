import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.bytedream.untis4j.responseObjects.Timetable

suspend fun sendMessage(config: Pushover, message: String) =
    ktor.post("https://api.pushover.net/1/messages.json") {
        parameter("token", config.apiKey)
        parameter("user", config.groupKey)
        parameter("message", message)
    }.let { response ->
        println(response.status)
        println(response.bodyAsText())
        println(response.headers)
    }

suspend fun sendLessonChange(config: Pushover, vararg changes: LessonChange) {
    for (change in changes) {
        when (change) {
            is LessonCancelledChange -> {
                sendMessage(config, "Lesson ${change.lessonTime} (${change.lessonName}) has been cancelled")
            }
            is LessonTeacherChange -> {
                sendMessage(config, "Lesson ${change.lessonTime} (${change.lessonName}) has been assigned a new teacher: ${change.teacher}")
            }
            is LessonRoomChange -> {
                sendMessage(config, "Lesson ${change.lessonTime} (${change.lessonName}) has been assigned a new room: ${change.room}")
            }
        }
    }

}