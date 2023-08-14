package com.actiangent.cuacagempa.core.data.repository

import com.actiangent.cuacagempa.core.common.result.Result
import com.actiangent.cuacagempa.core.data.repository.preferences.UserDataRepository
import com.actiangent.cuacagempa.core.data.repository.weather.WeatherRepository
import com.actiangent.cuacagempa.core.model.TemperatureOptions
import com.actiangent.cuacagempa.core.model.Weather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CompositeDistrictWeatherRepository @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val userDataRepository: UserDataRepository
) : DistrictWeatherRepository {

    override fun observeDistrictWeather(): Flow<Result<List<Weather>>> =
        userDataRepository.userData.map { it.areaId }
            .flatMapLatest { areaId ->
                weatherRepository.getCurrentWeather(
                    setOf(areaId),
                    TemperatureOptions.CELSIUS.name
                )
            }.map { weathers ->
                if (weathers.isEmpty()) {
                    Result.Loading
                } else {
                    Result.Success(weathers)
                }
            }.catch {
                emit(Result.Error(it))
            }

}