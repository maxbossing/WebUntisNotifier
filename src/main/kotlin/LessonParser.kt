import io.github.aakira.napier.Napier
import org.bytedream.untis4j.UntisUtils.LessonCode
import org.bytedream.untis4j.responseObjects.Timetable.Lesson
import java.time.LocalTime

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

fun Lesson.parseChange(): Pair<LessonChange, LessonChange?>? {
    val name = this.subjects[0].longName
    val time = lessonMap[startTime] ?: run {
        Napier.e("unknown lesson time: $startTime, name: $name")
        return null
    }
    if (code == LessonCode.CANCELLED) return LessonCancelledChange(time, name) to null

    Napier.d("teacher: ${teachers[0].longName}, original ${originalTeachers.getOrNull(0)?.longName}")

    val teacher =
        if (!originalTeachers.isEmpty()) LessonTeacherChange(time, name, teachers[0].longName) else null

    Napier.d("room: ${rooms[0].name}, original ${originalRooms.getOrNull(0)?.name}")

    val room = if (!originalRooms.isEmpty()) LessonRoomChange(time, name, rooms[0].name) else null

    if (teacher != null) {
        Napier.i("teacher irregularity at $name")
        return teacher to room
    }
    if (room != null) {
        Napier.i("room irregularity at $name")
        return room to null
    }

    return null
}