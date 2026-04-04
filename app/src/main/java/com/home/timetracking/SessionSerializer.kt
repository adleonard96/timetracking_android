package com.home.timetracking

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream


object SessionSerializer : Serializer<Session> {
    override val defaultValue: Session = Session(0, 0, false)
    override suspend fun readFrom(input: InputStream): Session =
        try {
            Json.decodeFromString<Session>(
                input.readBytes().decodeToString()
            )
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read sessions", serialization)
        }


    override suspend fun writeTo(t: Session, output: OutputStream) {
        output.write(
            Json.encodeToString(t)
                .encodeToByteArray()
        )
    }

}