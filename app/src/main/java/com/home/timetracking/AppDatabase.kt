package com.home.timetracking

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SessionData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
}