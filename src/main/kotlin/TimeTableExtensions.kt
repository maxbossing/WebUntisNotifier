import org.bytedream.untis4j.responseObjects.Timetable
import org.bytedream.untis4j.responseObjects.Timetable.Lesson

fun Timetable.lessons(): List<Lesson> {
    val list = mutableListOf<Lesson>()
    (0..<this.size).forEach { i ->
        list += get(i)
    }
    return list
}