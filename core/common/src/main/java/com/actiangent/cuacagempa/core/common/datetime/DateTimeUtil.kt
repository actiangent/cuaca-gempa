package com.actiangent.cuacagempa.core.common.datetime

import kotlinx.datetime.*
import java.time.format.DateTimeFormatter

object DateTimeUtil {
    val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")

    val jakartaTimeZone = TimeZone.of("Asia/Jakarta")
}

fun now() = Clock.System.now()

fun nowAsJakartaDateTime() = now().toJakartaDateTime()

fun Instant.toJakartaDateTime() = this.toLocalDateTime(DateTimeUtil.jakartaTimeZone)

fun LocalDateTime.jakartaDateTimeToInstant() = this.toInstant(DateTimeUtil.jakartaTimeZone)