package com.actiangent.cuacagempa.core.data.repository

import com.actiangent.cuacagempa.core.common.result.Result
import com.actiangent.cuacagempa.core.model.UserLocationWeather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface LocationWeatherRepository {

    val forceRequest: MutableStateFlow<Boolean>

    fun observeWeathers(): Flow<Result<UserLocationWeather>>

    fun forceUpdate()

}