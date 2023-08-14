package com.actiangent.cuacagempa.core.data.repository.weather

import com.actiangent.cuacagempa.core.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    suspend fun getCurrentWeather(
        districtIds: Set<String>,
        temperaturePreference: String,
    ): Flow<List<Weather>>

    suspend fun fetchUserCurrentWeather(
        latitude: Double,
        longitude: Double,
        province: String,
        saveCurrentAreaId: suspend (String) -> Unit
    )

    suspend fun fetchIndonesiaCurrentWeather()
}