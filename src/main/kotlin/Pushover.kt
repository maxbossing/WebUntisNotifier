import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.bytedream.untis4j.responseObjects.Timetable

private suspend fun sendMessage(config: Pushover, message: String) =
    ktor.post("https://api.pushover.net/1/messages.json") {
        parameter("token", config.apiKey)
        parameter("user", config.groupKey)
        parameter("message", message)
    }.let { response ->
        println(response.status)
        println(response.bodyAsText())
        println(response.headers)
    }

suspend fun sendCancelledLesson(config: Pushover, lessonNumber: Int, lesson: Timetable.Lesson) {
    sendMessage(config, "Entfall: Stunde $lessonNumber, ${lesson.subjects[0].name}")
}