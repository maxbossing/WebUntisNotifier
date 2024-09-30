package utils

import java.nio.file.Path
import kotlin.io.path.*


fun Path.createIfNotExists() {
    if (!exists()) {
        if (!parent.exists())
            parent.createDirectories()
        createFile()
    }
}

fun Path.createDirIfNotExists() {
    if (!exists()) {
        if (!parent.exists()) {
            parent.createDirectories()
        }
        createDirectory()
    }
}