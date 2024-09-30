package store

import com.toddway.shelf.FileStorage
import com.toddway.shelf.KotlinxSerializer
import com.toddway.shelf.Shelf
import com.toddway.shelf.get
import kotlinx.serialization.InternalSerializationApi
import utils.ifTrue
import java.io.File
import java.time.LocalDate

object LessonNotificationStore {
    @OptIn(InternalSerializationApi::class)
    private val shelf = Shelf(FileStorage(File("cache").apply { mkdirs() }), KotlinxSerializer())

    fun add(lessonTime: Int, lessonDate: LocalDate = LocalDate.now()): Boolean {
        has(lessonTime, lessonDate).ifTrue { return false }
        shelf.item("$lessonDate.$lessonTime").put(true)
        return true
    }

    fun has(lessonTime: Int, lessonDate: LocalDate = LocalDate.now()): Boolean = shelf.item("$lessonDate.$lessonTime").get<Boolean>() != null

    fun clear() = shelf.clear()
}