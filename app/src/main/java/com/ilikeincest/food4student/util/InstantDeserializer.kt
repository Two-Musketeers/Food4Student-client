package com.ilikeincest.food4student.util

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import kotlinx.datetime.Instant
import java.lang.reflect.Type
import kotlin.time.Duration.Companion.hours

class InstantDeserializer : JsonDeserializer<Instant> { 
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Instant {
        return Instant.parse(json.asString) + 7.hours
    }
}