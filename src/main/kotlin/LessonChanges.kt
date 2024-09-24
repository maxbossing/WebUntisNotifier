enum class LessonChangeType {
    CANCELLED,
    TEACHER,
    ROOM
}

abstract class LessonChange(
    val type: LessonChangeType,
    val lessonTime: Int,
    val lessonName: String,
)

class LessonCancelledChange(lessonTime: Int, lessonName: String)
    : LessonChange(LessonChangeType.CANCELLED, lessonTime, lessonName)

class LessonRoomChange(lessonTime: Int, lessonName: String, val room: String)
    : LessonChange(LessonChangeType.ROOM, lessonTime, lessonName)

class LessonTeacherChange(lessonTime: Int, lessonName: String, val teacher: String)
    : LessonChange(LessonChangeType.TEACHER, lessonTime, lessonName)