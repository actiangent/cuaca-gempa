package com.actiangent.cuacagempa.core.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import java.text.DecimalFormat
import kotlin.text.Typography.degree

data class Weather(
    val dateTime: LocalDateTime,
    val code: Int,
    val temperature: Double,
    val humidity: Int,
    val cardinal: String, // cardinal direction
    val knot: Int, // wind speed in knot
)

fun List<Weather>.firstByDate(date: LocalDate) =
    first { it.dateTime.date == date }

/**
 * 24-hour period weather forecast
 */
data class Forecast(
    val weathers: List<Weather>,
) {
    val summary = weathers
        .filter { it.dateTime.hour <= 14 }
        .maxBy { it.dateTime.hour }
    val minTemperature = weathers.minOf { it.temperature }
    val maxTemperature = weathers.maxOf { it.temperature }
    val minHumidity = weathers.minOf { it.humidity }
    val maxHumidity = weathers.maxOf { it.humidity }
}

fun List<Forecast>.firstByDate(date: LocalDate) =
    first { it.weathers.first().dateTime.date == date }

fun List<Weather>.chunkByDate(): List<Forecast> = groupBy { it.dateTime.date }.values
    .map { Forecast(it) }

private val df = DecimalFormat("0.#")

/**
 * Format [Double] to remove trailing zero if any, and append degree character
 */
fun Double.temperature() = "${df.format(this)}$degree"
