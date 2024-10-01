package untis

import config.TimeTableConfig
import utils.d
import org.bytedream.untis4j.UntisUtils.LessonCode
import org.bytedream.untis4j.responseObjects.Timetable.Lesson
import utils.ifFalse
import utils.w
import java.time.LocalTime

class LessonParser(val config: TimeTableConfig) {

    private val lessonMap = mapOf<LocalTime, Int>(
        LocalTime.parse("07:45:00"/*8:30*/) to 1,
        LocalTime.parse("08:30:00"/*9:15*/) to 2,
        // Break 9:15-9:30
        LocalTime.parse("09:30:00"/*10:15*/) to 3,
        LocalTime.parse("10:15:00"/*11:00*/) to 4,
        // Break 11:00-11:15
        LocalTime.parse("11:15:00"/*12:00*/) to 5,
        LocalTime.parse("12:00:00"/*12:45*/) to 6,
        LocalTime.parse("12:45:00"/*13:30*/) to 7, // Usually free
        LocalTime.parse("13:30:00"/*14:15*/) to 8,
        LocalTime.parse("14:15:00"/*15:00*/) to 9,
        // Break 15:00-15:05
        LocalTime.parse("15:05:00"/*15:50*/) to 10,
        LocalTime.parse("15:50:00"/*16:35*/) to 11,
    )

    fun parseChange(lesson: Lesson): List<LessonChange>? {
        d("parsing lesson (${lesson.subjects[0].longName}) at (${lesson.startTime})")
        val name = lesson.subjects[0].longName
        val time = lessonMap[lesson.startTime] ?: run {
            w("invalid lesson time (${lesson.startTime})")
            return null
        }

        if (lesson.code == LessonCode.CANCELLED) return listOf(
            LessonChange(
                LessonChangeType.CANCELLED,
                time,
                name,
                null
            )
        ).also { d("found cancelled lesson ($time, $name)") }

        val changes = mutableListOf<LessonChange>()

        (lesson.originalTeachers.isEmpty()).ifFalse {
            changes += LessonChange(LessonChangeType.TEACHER, time, name, lesson.teachers[0].longName)
            d("found changed teacher ($time, $name)")
        }

        (lesson.originalRooms.isEmpty()).ifFalse {
            changes += LessonChange(LessonChangeType.ROOM, time, name, lesson.rooms[0].name)
            d("found changed room ($time, $name)")
        }

        return changes.takeIf { it.isNotEmpty() }
    }
}
