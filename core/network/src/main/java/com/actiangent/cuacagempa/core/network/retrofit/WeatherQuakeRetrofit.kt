package com.actiangent.cuacagempa.core.network.retrofit

import com.actiangent.cuacagempa.core.network.RemoteWeatherDataSource
import com.actiangent.cuacagempa.core.network.model.NetworkRegencyWeather
import com.actiangent.cuacagempa.core.network.model.NetworkResponseWeatherArea
import com.actiangent.cuacagempa.core.network.model.NetworkResponseWeatherData
import com.actiangent.cuacagempa.core.network.model.NetworkResponseWeatherParameter
import com.actiangent.cuacagempa.core.network.model.NetworkWeather
import com.actiangent.cuacagempa.core.network.retrofit.WeatherRetrofit.NetworkWeatherParser.extract
import kotlinx.datetime.Instant
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Inject
import javax.inject.Singleton

const val baseUrl = "https://data.bmkg.go.id/DataMKG/"

private interface WeatherApi {

    @GET("MEWS/DigitalForecast/DigitalForecast-{provinceEndpoint}.xml")
    suspend fun getProvinceWeather(@Path("provinceEndpoint") endpoint: String): NetworkResponseWeatherData
}

private interface EarthQuakeApi

@Singleton
class WeatherRetrofit @Inject constructor(
    retrofit: Retrofit
) : RemoteWeatherDataSource {

    private val weatherApi = retrofit.create(WeatherApi::class.java)

    override suspend fun getRegencyWeathers(
        provinceEndpoint: String,
        regencyId: String
    ): NetworkRegencyWeather = weatherApi.getProvinceWeather(provinceEndpoint)
        .extract(regencyId)

    private object NetworkWeatherParser {

        fun NetworkResponseWeatherData.extract(regencyId: String): NetworkRegencyWeather =
            forecast.data.byId(regencyId).toNetworkRegencyWeather()

        private fun List<NetworkResponseWeatherArea>.byId(regencyId: String) =
            first { it.id == regencyId }

        private fun NetworkResponseWeatherArea.toNetworkRegencyWeather(): NetworkRegencyWeather =
            parameters?.let { parameters ->
                // each parameter representing value with corresponding id's
                // (weather, humidity(hu), temperature(t), wind direction (wd))
                val filteredParameters = parameters
                    .filter { p -> p.id in setOf("hu", "t", "weather", "wd", "ws") }
                    .sortedBy { id }

                val humidities = filteredParameters[0].extractHumidityValues()
                val temperatures = filteredParameters[1].extractTemperatureValues()
                val timestamps = filteredParameters[2].extractTimestampValues()
                val weathers = filteredParameters[2].extractWeatherValues()
                val windDirections = filteredParameters[3].extractWindDirectionValues()
                val windSpeeds = filteredParameters[4].extractWindSpeedValues()

                val networkWeathers = timestamps.mapIndexed { index, timestamp ->
                    NetworkWeather(
                        timestamp = timestamp,
                        weatherCode = weathers[index],
                        temperatureCelsius = temperatures[index].first,
                        temperatureFahrenheit = temperatures[index].second,
                        humidity = humidities[index],
                        windDirectionCardinal = windDirections[index].first,
                        windDirectionDegree = windDirections[index].second,
                        windSpeedKnot = windSpeeds[index].first,
                        windSpeedMph = windSpeeds[index].second.first,
                        windSpeedKph = windSpeeds[index].second.second,
                        windSpeedMps = windSpeeds[index].second.third,
                    )
                }

                NetworkRegencyWeather(
                    networkWeathers = networkWeathers,
                    id = id,
                )
            } ?: NetworkRegencyWeather(
                networkWeathers = emptyList(),
                id = id,
            )

        private fun NetworkResponseWeatherParameter.extractTimestampValues(): List<Instant> =
            if (id == "weather") {
                timeRanges.map { it.datetime }
            } else emptyList()

        private fun NetworkResponseWeatherParameter.extractHumidityValues(): List<Int> =
            if (id == "hu") {
                timeRanges.map { timeRange -> timeRange.values.first().value.toInt() }
            } else emptyList()

        private fun NetworkResponseWeatherParameter.extractTemperatureValues(): List<Pair<Double, Double>> =
            if (id == "t") {
                timeRanges.map { timeRange ->
                    with(timeRange) {
                        val celsius = values.first { it.unit == "C" }.value.toDouble()
                        val fahrenheit = values.first { it.unit == "F" }.value.toDouble()
                        celsius to fahrenheit
                    }
                }
            } else emptyList()

        private fun NetworkResponseWeatherParameter.extractWeatherValues(): List<Int> =
            if (id == "weather") {
                timeRanges.map { timeRange -> timeRange.values[0].value.toInt() }
            } else emptyList()

        private fun NetworkResponseWeatherParameter.extractWindDirectionValues(): List<Pair<String, Double>> =
            if (id == "wd") {
                timeRanges.map { timeRange ->
                    with(timeRange) {
                        val cardinal = values.first { it.unit == "CARD" }.value
                        val degree = values.first { it.unit == "deg" }.value.toDouble()
                        cardinal to degree
                    }
                }
            } else emptyList()

        private fun NetworkResponseWeatherParameter.extractWindSpeedValues(): List<Pair<Int, Triple<Double, Double, Double>>> =
            if (id == "ws") {
                timeRanges.map { timeRange ->
                    with(timeRange) {
                        val knot = values.first { it.unit == "Kt" }.value.toInt()
                        val mph = values.first { it.unit == "MPH" }.value.toDouble()
                        val kph = values.first { it.unit == "KPH" }.value.toDouble()
                        val mps = values.first { it.unit == "MS" }.value.toDouble()
                        knot to Triple(mph, kph, mps)
                    }
                }
            } else emptyList()
    }
}