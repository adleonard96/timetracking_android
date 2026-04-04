package com.home.timetracking

import android.content.Context
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val Context.dataStore by dataStore(
    fileName = "sessions.json",
    serializer = SessionSerializer
)

class DataStoreRepository(private val context: Context) {
//    private val json = Json { ignoreUnknownKeys = true }
//
//    suspend fun saveName(name: String) {
//        context.dataStore.edit {
//            it[stringPreferencesKey("name")] = name
//        }
//    }
//
//    suspend fun saveSessions(sessions: List<Session>) {
//        val key = stringPreferencesKey("sessions")
//        context.dataStore.edit {
//                prefs -> prefs[key] = json.encodeToString(sessions)
//        }
//    }

//    suspend fun loadSessions(): List<Session> {
//
//    }
}