import org.bytedream.untis4j.responseObjects.Timetable.Lesson

val cancelledLessonCache = mutableListOf<Lesson>()

fun Lesson.messageHasGoneOut(): Boolean = cancelledLessonCache.contains(this)
fun Lesson.messageSent() = cancelledLessonCache.add(this)