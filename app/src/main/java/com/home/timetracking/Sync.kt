package com.home.timetracking

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.text.SimpleDateFormat


fun syncSessions(sessions: List<Session>) {
    if (sessions.isEmpty()) {
        return
    }
    val client = OkHttpClient()
    val sessionStrings = ArrayList<String>()
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    val timeFormat = SimpleDateFormat("HH:mm:ss")
    val dayOfWeek = SimpleDateFormat("E")
    for (session in sessions) {
        if (session.endTimeMillis != null) {
            sessionStrings.add(
                "{ \"start\": \"${sdf.format(session.startTimeMillis)}T${timeFormat.format(session.startTimeMillis)}\",  \"stop\": \"${
                    sdf.format(
                        session.endTimeMillis
                    )
                }T${timeFormat.format(session.endTimeMillis)}\", \"dayOfWeek\": \"${
                    dayOfWeek.format(
                        session.startTimeMillis
                    )
                }\"}"
            )
        }
    }
    val joined = sessionStrings.joinToString()
    val mediaType = "application/json".toMediaType()
    val body = "[${joined}]".toRequestBody(mediaType)
    try {
        val request =
            Request.Builder().url("http://192.168.1.21:8081/sync").method("POST", body).build()
        client.newCall(request).execute()
    } catch (e: Exception) {
        println(e)
    }
}