package com.actiangent.cuacagempa.core.data.repository

import com.actiangent.cuacagempa.core.common.result.Result
import com.actiangent.cuacagempa.core.model.Weather
import kotlinx.coroutines.flow.Flow

interface DistrictWeatherRepository {

    fun observeDistrictWeather(): Flow<Result<List<Weather>>>
}