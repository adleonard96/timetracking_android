package com.home.timetracking

import androidx.room.ColumnInfo

data class SessionData(
    @ColumnInfo(name = "startTime") val startTimeMillis: Long,
    @ColumnInfo(name = "endTime") val endTimeMillis: Long?,
)
