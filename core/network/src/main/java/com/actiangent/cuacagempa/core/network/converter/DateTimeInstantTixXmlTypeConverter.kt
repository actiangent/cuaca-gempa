package com.actiangent.cuacagempa.core.network.converter

import com.tickaroo.tikxml.TypeConverter
import kotlinx.datetime.Instant
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.toInstant
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime as JavaLocalDateTime

class DateTimeInstantTixXmlTypeConverter : TypeConverter<Instant> {

    private val dateTimePattern = "yyyyMMddHHmm"
    private val dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimePattern)
    private val defaultUtcOffset = UtcOffset.ZERO // timestamp from response is UTC+0

    override fun read(value: String): Instant {
        val dateTime = if (value.length > dateTimePattern.length) {
            value.substring(dateTimePattern.indices)
        } else {
            value
        }

        // workaround for kotlinx-datetime parse from string using java.time.format.DateTimeFormatter
        val kotlinLocalDateTime = JavaLocalDateTime.parse(
            dateTime,
            dateTimeFormatter
        ).toKotlinLocalDateTime()

        return kotlinLocalDateTime.toInstant(defaultUtcOffset)
    }

    override fun write(value: Instant): String {
        return value.toString()
    }
}