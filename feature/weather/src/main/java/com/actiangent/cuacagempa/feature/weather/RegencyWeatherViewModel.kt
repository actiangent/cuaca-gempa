package com.actiangent.cuacagempa.feature.weather

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.actiangent.cuacagempa.core.common.result.Result
import com.actiangent.cuacagempa.core.common.result.asResult
import com.actiangent.cuacagempa.core.data.repository.RegencyRepository
import com.actiangent.cuacagempa.core.data.repository.preferences.UserDataRepository
import com.actiangent.cuacagempa.core.data.repository.weather.WeatherRepository
import com.actiangent.cuacagempa.core.model.Forecast
import com.actiangent.cuacagempa.core.model.SaveableRegency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegencyWeatherViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val regencyRepository: RegencyRepository,
    private val weatherRepository: WeatherRepository,
    private val userDataRepository: UserDataRepository,
) : ViewModel() {

    private val regencyId: String = checkNotNull(savedStateHandle["regencyId"])
    val regencyWeatherUiState: StateFlow<RegencyWeatherUiState> = userDataRepository.userData
        .flatMapLatest { userData ->
            combine(
                regencyRepository.getRegency(regencyId).asResult(),
                weatherRepository.getRemoteWeatherForecast(regencyId, userData.temperatureUnit)
            ) { regency, forecasts ->
                regency to forecasts
            }
        }
        .map { (regencyResult, forecastsResult) ->
            when (regencyResult) {
                Result.Loading -> RegencyWeatherUiState.Loading

                is Result.Success -> {
                    when (forecastsResult) {
                        Result.Loading -> RegencyWeatherUiState.Loading
                        is Result.Success -> {
                            RegencyWeatherUiState.Success(
                                regency = SaveableRegency(
                                    regency = regencyResult.data,
                                    isSaved = regencyResult.data.id in userDataRepository.userData.first().userRegencyIds
                                ),
                                forecasts = forecastsResult.data
                            )
                        }

                        is Result.Error -> RegencyWeatherUiState.Error(message = forecastsResult.message)
                    }
                }

                is Result.Error -> RegencyWeatherUiState.Error(message = regencyResult.message)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = RegencyWeatherUiState.Loading
        )


    fun saveRegency(saved: Boolean) {
        viewModelScope.launch {
            userDataRepository.setUserRegencyIdSaved(regencyId, saved)
        }
    }
}

sealed interface RegencyWeatherUiState {
    object Loading : RegencyWeatherUiState
    data class Success(
        val regency: SaveableRegency,
        val forecasts: List<Forecast>,
    ) : RegencyWeatherUiState

    data class Error(val message: String) : RegencyWeatherUiState
}
