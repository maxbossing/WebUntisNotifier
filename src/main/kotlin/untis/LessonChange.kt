package untis

enum class LessonChangeType {
    CANCELLED,
    TEACHER,
    ROOM
}

data class LessonChange(
    val type: LessonChangeType,
    val lessonTime: Int,
    val lessonName: String,
    val change: String?
)