package com.actiangent.cuacagempa.core.common.datetime

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter

object DateTimeUtil {
    val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")

    val jakartaTimeZone = TimeZone.of("Asia/Jakarta")
}

fun now() = Clock.System.now()

fun LocalDate.text(): String = "$dayOfMonth/$monthNumber/$year"

fun LocalDateTime.text(): String = "$dayOfMonth/$monthNumber/$year - $hour:$minute:$second"

fun nowAsJakartaDateTime() = now().toJakartaDateTime()

fun Instant.toJakartaDateTime() = this.toLocalDateTime(DateTimeUtil.jakartaTimeZone)

fun LocalDateTime.jakartaDateTimeToInstant() = this.toInstant(DateTimeUtil.jakartaTimeZone)