package com.actiangent.cuacagempa.core.network.model

import kotlinx.datetime.Instant

data class NetworkRegencyWeather(
    val networkWeathers: List<NetworkWeather>,
    val id: String = "0",
)

data class NetworkWeather(
    val datetime: Instant,
    val weatherCode: Int,
    val temperatureCelsius: Double,
    val temperatureFahrenheit: Double,
    val humidity: Int,
    val windDirectionCardinal: String,
    val windDirectionDegree: Double,
    val windSpeedKnot: Int,
    val windSpeedMph: Double,
    val windSpeedKph: Double,
    val windSpeedMps: Double,
)
