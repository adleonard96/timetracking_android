package com.home.timetracking

import androidx.room.Insert
import androidx.room.Query

interface SessionDao {
    @Query("Select * from sessions")
    fun loadAllSessions(): List<SessionData>

    @Query("DELETE FROM sessions")
    fun clearSessions()

    @Insert
    fun insertAll(vararg sessions: Session)
}