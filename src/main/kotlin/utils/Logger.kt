package utils

import debug
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.system.exitProcess

private fun log(msg: Any, label: String) =
    DateTimeFormatter
        .ISO_DATE_TIME
        .withZone(ZoneOffset.systemDefault())
        .format(Instant.now()).let {
            println("[$it] $label: $msg")
        }

fun i(msg: Any) = log(msg, "i")
fun d(msg: Any) = debug.ifTrue { log(msg, "d") }
fun w(msg: Any) = log(msg, "w")
fun e(msg: Any): Nothing = log(msg, "e").let { exitProcess(-1) }