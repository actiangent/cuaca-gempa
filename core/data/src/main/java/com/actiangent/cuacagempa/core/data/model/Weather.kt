package com.actiangent.cuacagempa.core.data.model

import com.actiangent.cuacagempa.core.database.model.RegencyWeatherEntity
import com.actiangent.cuacagempa.core.network.model.NetworkWeather

fun NetworkWeather.asRegencyWeatherEntity(
    regencyId: String
) = RegencyWeatherEntity(
    timestamp = timestamp,
    weatherCode = weatherCode,
    temperatureCelsius = temperatureCelsius,
    temperatureFahrenheit = temperatureFahrenheit,
    humidity = humidity,
    windDirectionCardinal = windDirectionCardinal,
    windDirectionDegree = windDirectionDegree,
    windSpeedKnot = windSpeedKnot,
    windSpeedMph = windSpeedMph,
    windSpeedKph = windSpeedKph,
    windSpeedMps = windSpeedMps,
    regencyId = regencyId
)