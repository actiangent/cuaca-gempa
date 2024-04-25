package com.actiangent.cuacagempa.core.data.repository.weather

import com.actiangent.cuacagempa.core.common.result.Result
import com.actiangent.cuacagempa.core.data.WeatherSyncable
import com.actiangent.cuacagempa.core.model.Forecast
import com.actiangent.cuacagempa.core.model.RegencyForecasts
import com.actiangent.cuacagempa.core.model.TemperatureUnit
import kotlinx.coroutines.flow.Flow

interface WeatherRepository : WeatherSyncable {

    fun getLocalRegencyWeatherForecast(
        regencyId: String,
        temperatureUnit: TemperatureUnit
    ): Flow<Result<RegencyForecasts>>

    fun getRemoteWeatherForecast(
        regencyId: String,
        temperatureUnit: TemperatureUnit
    ): Flow<List<Forecast>>
}