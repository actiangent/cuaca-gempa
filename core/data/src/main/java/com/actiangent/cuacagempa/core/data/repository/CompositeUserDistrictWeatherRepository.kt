package com.actiangent.cuacagempa.core.data.repository

import com.actiangent.cuacagempa.core.common.result.Result
import com.actiangent.cuacagempa.core.data.repository.preferences.UserDataRepository
import com.actiangent.cuacagempa.core.data.repository.weather.WeatherRepository
import com.actiangent.cuacagempa.core.model.RegencyForecasts
import com.actiangent.cuacagempa.core.model.TemperatureUnit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CompositeUserDistrictWeatherRepository @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val userDataRepository: UserDataRepository,
) : UserDistrictWeatherRepository {

    override val userRegencies: Flow<Set<String>>
        get() = userDataRepository.userData.map { it.userRegencyIds }

    override fun observeUserRegencyWeatherForecast(
        regencyId: String
    ): Flow<Result<RegencyForecasts>> =
        weatherRepository.getLocalRegencyWeatherForecast(
            regencyId,
            TemperatureUnit.CELSIUS
        )
}