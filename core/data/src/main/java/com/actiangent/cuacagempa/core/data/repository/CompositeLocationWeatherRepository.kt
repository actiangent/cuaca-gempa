package com.actiangent.cuacagempa.core.data.repository

import com.actiangent.cuacagempa.core.common.result.Result
import com.actiangent.cuacagempa.core.data.networkBoundResource
import com.actiangent.cuacagempa.core.data.repository.preferences.UserDataRepository
import com.actiangent.cuacagempa.core.data.repository.weather.WeatherRepository
import com.actiangent.cuacagempa.core.model.TemperaturePreferences
import com.actiangent.cuacagempa.core.model.UserLocationWeather
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class CompositeLocationWeatherRepository @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val userDataRepository: UserDataRepository
) : LocationWeatherRepository {

    override val forceRequest = MutableStateFlow(true)

    private val userLocationData = userDataRepository.userLocationData
        .distinctUntilChanged { old, new ->
            ((old.latitude == new.latitude) and (old.longitude == new.longitude)) or (old.province == new.province)
        }

    override fun observeWeathers(): Flow<Result<UserLocationWeather>> = combine(
        userLocationData, userDataRepository.districtId, forceRequest
    ) { locationCache, districtId, forceRequest ->
        val weathers = networkBoundResource(getLocalData = {
            weatherRepository.getLocalCurrentDistrictWeathers(
                districtId,
                TemperaturePreferences.CELSIUS.name
            )
        }, shouldFetch = {
            locationCache.isLocationCached and forceRequest
        }, requestFromNetwork = {
            weatherRepository.fetchCurrentDistrictWeathers(
                locationCache.latitude!!,
                locationCache.longitude!!,
                locationCache.province!!,
            )
        }, saveNetworkRequest = { data ->
            data.first.districtId.also { userDataRepository.setDistrictId(it) }
            weatherRepository.insertCurrentDistrictWeathers(data)
        })

        UserLocationWeather(locationCache.province ?: "", weathers)
    }.map {
        if (it.weathers.isEmpty()) {
            forceUpdate()
            Result.Loading
        } else {
            Result.Success(it)
        }
    }.catch {
        emit(Result.Error(it))
    }.onEach {
        forceRequest.update { false }
    }

    override fun forceUpdate() {
        forceRequest.update { true }
    }

}