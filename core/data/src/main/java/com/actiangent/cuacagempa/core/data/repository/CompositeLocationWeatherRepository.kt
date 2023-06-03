package com.actiangent.cuacagempa.core.data.repository

import android.util.Log
import com.actiangent.cuacagempa.core.common.result.Result
import com.actiangent.cuacagempa.core.data.networkBoundResource
import com.actiangent.cuacagempa.core.data.repository.preferences.UserDataRepository
import com.actiangent.cuacagempa.core.data.repository.weather.WeatherRepository
import com.actiangent.cuacagempa.core.model.UserLocationWeather
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class CompositeLocationWeatherRepository @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val userDataRepository: UserDataRepository
) : LocationWeatherRepository {

    override val forceRequest = MutableStateFlow(true)

    private val userData = userDataRepository.userData
        .onEach {
            Log.d("weathers", "userData: $it")
        }
        .takeWhile { userData ->
            userData.isLocationCached
        }

    override fun observeWeathers(): Flow<Result<UserLocationWeather>> = combine(
        userData, forceRequest
    ) { userData, forceRequest ->
        val weathers = networkBoundResource(getLocalData = {
            weatherRepository.getLocalCurrentDistrictWeathers(
                userData.districtId,
                userData.temperatureOption.name
            )
        }, shouldFetch = {
            forceRequest
        }, requestFromNetwork = {
            weatherRepository.fetchCurrentDistrictWeathers(
                userData.latitude,
                userData.longitude,
                userData.provinceEndpoint,
            )
        }, saveNetworkRequest = { data ->
            Log.d("weathers", "districtId: ${data.first.districtId}")
            data.first.districtId.also { userDataRepository.setDistrictId(it) }
            weatherRepository.insertCurrentDistrictWeathers(data)
        })

        Log.d("weathers", "forceRequest: $forceRequest")
        Log.d("weathers", "userData: $userData")

        UserLocationWeather(userData.provinceEndpoint, weathers)
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