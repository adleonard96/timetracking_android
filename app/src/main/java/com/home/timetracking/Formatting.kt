package com.home.timetracking

import java.text.SimpleDateFormat
import java.util.*

fun formatTime(millis: Long): String {
    val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return formatter.format(Date(millis))
}

fun formatDuration(millis: Long): String {
    val seconds = millis / 1000
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60

    return "%02d:%02d:%02d".format(hours, minutes, secs)
}
