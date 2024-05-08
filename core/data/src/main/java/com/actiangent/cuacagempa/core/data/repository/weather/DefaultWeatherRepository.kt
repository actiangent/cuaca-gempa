package com.actiangent.cuacagempa.core.data.repository.weather

import com.actiangent.cuacagempa.core.common.result.Result
import com.actiangent.cuacagempa.core.data.WeatherSynchronizer
import com.actiangent.cuacagempa.core.data.changeRegencyWeatherSync
import com.actiangent.cuacagempa.core.data.model.asRegencyWeatherEntity
import com.actiangent.cuacagempa.core.database.dao.ProvinceDao
import com.actiangent.cuacagempa.core.database.dao.RegencyDao
import com.actiangent.cuacagempa.core.database.dao.WeatherDao
import com.actiangent.cuacagempa.core.location.LocationDataSource
import com.actiangent.cuacagempa.core.model.Forecast
import com.actiangent.cuacagempa.core.model.TemperatureUnit
import com.actiangent.cuacagempa.core.model.Weather
import com.actiangent.cuacagempa.core.model.chunkByDate
import com.actiangent.cuacagempa.core.network.RemoteWeatherDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

class DefaultWeatherRepository @Inject constructor(
    private val locationDataSource: LocationDataSource,
    private val provinceDao: ProvinceDao,
    private val regencyDao: RegencyDao,
    private val remoteWeatherDataSource: RemoteWeatherDataSource,
    private val regencyWeatherDao: WeatherDao
) : WeatherRepository {

    override fun getLocalRegencyWeatherForecast(
        regencyId: String,
        temperatureUnit: TemperatureUnit
    ): Flow<Result<List<Forecast>>> =
        regencyWeatherDao.getRegencyWeatherForecast(regencyId, temperatureUnit.name)
            .map { weathers ->
                try {
                    if (weathers.isEmpty()) {
                        Result.Loading
                    } else {
                        Result.Success(
                            data = weathers.chunkByDate()
                        )
                    }
                } catch (e: Throwable) {
                    Result.Error(exception = e)
                }
            }


    override fun getRemoteWeatherForecast(
        regencyId: String,
        temperatureUnit: TemperatureUnit
    ): Flow<List<Forecast>> = flow {
        val provinceEntity = provinceDao.getOneOffProvinceEntityByRegencyId(regencyId)
        val networkRegencyWeathers = remoteWeatherDataSource.getRegencyWeathers(
            provinceEndpoint = provinceEntity.endpoint,
            regencyId = regencyId
        )

        val weathers = networkRegencyWeathers.networkWeathers.map { networkWeather ->
            with(networkWeather) {
                Weather(
                    timestamp = timestamp.toLocalDateTime(TimeZone.of("Asia/Jakarta")),
                    code = weatherCode,
                    temperature = when (temperatureUnit) {
                        TemperatureUnit.CELSIUS -> temperatureCelsius
                        TemperatureUnit.FAHRENHEIT -> temperatureFahrenheit
                    },
                    humidity = humidity,
                    cardinal = windDirectionCardinal,
                    knot = windSpeedKnot
                )
            }
        }

        val forecasts = weathers.chunkByDate()
        emit(
            forecasts.subList(0, forecasts.size - 1)
        )
    }

    override suspend fun syncByRegencyIdWith(
        regencyId: String,
        synchronizer: WeatherSynchronizer
    ) = synchronizer.changeRegencyWeatherSync(
        weatherFetcher = {
            val provinceEntity = provinceDao.getOneOffProvinceEntityByRegencyId(regencyId)
            val networkRegencyWeathers = remoteWeatherDataSource.getRegencyWeathers(
                provinceEndpoint = provinceEntity.endpoint,
                regencyId = regencyId
            )

            networkRegencyWeathers
        },
        modelInserter = { networkRegencyWeather ->
            val crossRefRegencyWeatherEntities = networkRegencyWeather.networkWeathers
                .map { networkWeather ->
                    networkWeather.asRegencyWeatherEntity(regencyId)
                }

            regencyWeatherDao.insertRegencyWeathers(crossRefRegencyWeatherEntities)
        },
        modelDeleter = {
            regencyWeatherDao.deleteOldWeatherCache()
        }
    )

    override suspend fun syncByLocationWith(synchronizer: WeatherSynchronizer) =
        synchronizer.changeRegencyWeatherSync(
            weatherFetcher = {
                val location = locationDataSource.requestUpdatedLocation(Dispatchers.IO)
                val provinceEntity = provinceDao.getOneOffProvinceEntityByNearestLatLon(
                    latitude = location.latitude,
                    longitude = location.longitude
                )
                val regencyEntity = regencyDao.getOneOffRegencyEntityByProvinceIdAndNearestLatLon(
                    latitude = location.latitude,
                    longitude = location.longitude,
                    provinceId = provinceEntity.id
                )
                val networkRegencyWeathers = remoteWeatherDataSource.getRegencyWeathers(
                    provinceEndpoint = provinceEntity.endpoint,
                    regencyId = regencyEntity.id
                )

                networkRegencyWeathers

            },
            modelInserter = { networkRegencyWeather ->
                val regencyId = networkRegencyWeather.id
                val crossReffRegencyWeatherEntities = networkRegencyWeather.networkWeathers
                    .map { networkWeather ->
                        networkWeather.asRegencyWeatherEntity(regencyId)
                    }

                regencyWeatherDao.insertRegencyWeathers(crossReffRegencyWeatherEntities)
            },
            modelDeleter = {
                regencyWeatherDao.deleteOldWeatherCache()
            }
        )
}

