package com.actiangent.cuacagempa.core.network.converter

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import kotlinx.datetime.Instant
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.toInstant
import kotlinx.datetime.toKotlinLocalDateTime
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DatetimeInstantGsonTypeAdapter : JsonDeserializer<Instant> {

    private val dateTimePattern = "yyyy-MM-dd'T'HH:mm:ss"
    private val dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimePattern)
    private val defaultUtcOffset = UtcOffset.ZERO // timestamp from response is UTC+0

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Instant? = json?.toString()?.let { value ->
        val dateTime = if (value.length > dateTimePattern.length - 2) {
            value.substring(startIndex = 0, endIndex = dateTimePattern.length - 1)
        } else {
            value
        }

        val kotlinLocalDateTime = LocalDateTime.parse(
            dateTime.replace("\"", ""),
            dateTimeFormatter
        ).toKotlinLocalDateTime()

        kotlinLocalDateTime.toInstant(defaultUtcOffset)
    }
}