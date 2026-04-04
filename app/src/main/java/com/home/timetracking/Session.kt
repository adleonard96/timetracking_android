package com.home.timetracking

import kotlinx.serialization.Serializable

@Serializable
data class Session(
    val startTimeMillis: Long,
    val endTimeMillis: Long?,
    val synced: Boolean
) {
    val durationMillis: Long
        get() = (endTimeMillis ?: System.currentTimeMillis()) - startTimeMillis
}
