package com.actiangent.cuacagempa.core.database.util

import androidx.room.TypeConverter
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class InstantTypeConverter {

    @TypeConverter
    fun parseInstant(instant: String?): Instant? {
        return instant?.let { instant.toInstant() }
    }

    @TypeConverter
    fun instantToString(instant: Instant?): String? {
        return instant?.toString()
    }

}

class LocalDateTimeTypeConverter {

    @TypeConverter
    fun parseDateTime(dateTime: String?): LocalDateTime? {
        return dateTime?.let { dateTime.toLocalDateTime() }
    }

    @TypeConverter
    fun instantToString(dateTime: LocalDateTimeTypeConverter?): String? {
        return dateTime?.toString()
    }

}
