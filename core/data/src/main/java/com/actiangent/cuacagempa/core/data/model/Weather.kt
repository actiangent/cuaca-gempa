package com.actiangent.cuacagempa.core.data.model

import com.actiangent.cuacagempa.core.database.model.DistrictEntity
import com.actiangent.cuacagempa.core.database.model.WeatherEntity
import com.actiangent.cuacagempa.core.network.model.NetworkWeatherArea
import kotlinx.datetime.Instant

fun NetworkWeatherArea.asEntities(): Pair<DistrictEntity, List<WeatherEntity>> {
    val district = DistrictEntity(id)

    return if (parameters == null) {
        Pair(district, emptyList())
    } else {
        val nonNullParameters = parameters!!

        val instantTimestamps = mutableListOf<Instant>()

        val threeDayWeatherCode = nonNullParameters
            .find { it.id == "weather" }!!.timeRanges.map { it.values[0].value.toInt() }

        val threeDayWeatherHumidity = nonNullParameters
            .find { it.id == "hu" }!!.timeRanges.map { it.values[0].value.toInt() }

        val threeDayWeatherTemperature = nonNullParameters
            .find { it.id == "t" }!!.timeRanges
            .map { timerange ->
                instantTimestamps.add(timerange.datetime)
                timerange.values.map { value -> value.value.toDouble() }
            }

        val weathers = mutableListOf<WeatherEntity>()
        for (i in instantTimestamps.indices) {
            val weather = WeatherEntity(
                timestamp = instantTimestamps[i],
                code = threeDayWeatherCode[i],
                celsius = threeDayWeatherTemperature[i][0],
                fahrenheit = threeDayWeatherTemperature[i][1],
                humidity = threeDayWeatherHumidity[i],
            )

            weathers.add(weather)
        }

        Pair(district, weathers)
    }
}
