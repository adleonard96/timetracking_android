package com.home.timetracking

data class Session(
    val startTimeMillis: Long,
    val endTimeMillis: Long?,
    val synced: Boolean
) {
    val durationMillis: Long
        get() = (endTimeMillis ?: System.currentTimeMillis()) - startTimeMillis
}
