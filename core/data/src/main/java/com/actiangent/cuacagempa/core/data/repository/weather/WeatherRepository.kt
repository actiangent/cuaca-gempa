package com.actiangent.cuacagempa.core.data.repository.weather

import com.actiangent.cuacagempa.core.database.model.DistrictEntity
import com.actiangent.cuacagempa.core.database.model.WeatherEntity
import com.actiangent.cuacagempa.core.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    suspend fun getLocalCurrentDistrictWeathers(
        districtId: String,
        temperaturePreferences: String
    ): List<Weather>

    suspend fun fetchCurrentDistrictWeathers(
        lat: Double,
        lon: Double,
        province: String,
    ): Pair<DistrictEntity, List<WeatherEntity>>

    suspend fun insertCurrentDistrictWeathers(
        entities: Pair<DistrictEntity, List<WeatherEntity>>
    )

}