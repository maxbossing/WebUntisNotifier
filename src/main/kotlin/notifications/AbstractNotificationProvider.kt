package notifications

import config.NotificationConfig
import untis.LessonChange
import untis.LessonChangeType


abstract class AbstractNotificationProvider<T: NotificationConfig>(private val config: T) {
    protected abstract suspend fun sendMessage(message: String)

    suspend fun sendChanges(vararg changes: LessonChange) = changes.forEach {
        when (it.type) {
            LessonChangeType.CANCELLED -> {
                sendMessage("Lesson ${it.lessonTime} ${it.lessonName} has been cancelled")
            }
            LessonChangeType.ROOM -> {
                sendMessage("Lesson ${it.lessonTime} (${it.lessonName}) has been assigned a new room: ${it.change}")
            }
            LessonChangeType.TEACHER -> {
                sendMessage( "Lesson ${it.lessonTime} (${it.lessonName}) has been assigned a new teacher: ${it.change}")
            }
        }
    }
}