package com.actiangent.cuacagempa.core.data.repository

import com.actiangent.cuacagempa.core.common.result.Result
import com.actiangent.cuacagempa.core.model.Forecast
import com.actiangent.cuacagempa.core.model.RegencyForecasts
import com.actiangent.cuacagempa.core.model.Weathers
import kotlinx.coroutines.flow.Flow

interface UserDistrictWeatherRepository {

    val userRegencies: Flow<Set<String>>

    fun observeUserRegencyWeatherForecast(regencyId: String): Flow<Result<RegencyForecasts>>
}