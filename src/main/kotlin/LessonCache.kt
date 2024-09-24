import io.github.aakira.napier.Napier
import org.bytedream.untis4j.responseObjects.Timetable.Lesson

// The one true cache
val lessonCache = mutableSetOf<Pair<Int, LessonChangeType>>()


fun cacheLesson(lesson: Int, change: LessonChangeType) : Boolean {
    if (lessonCache.contains(lesson to change)) return false
    Napier.d("Adding lesson $lesson with change $change to cache")
    lessonCache.add(lesson to change)
    return true
}