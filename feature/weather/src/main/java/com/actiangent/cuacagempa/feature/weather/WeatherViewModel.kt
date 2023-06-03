package com.actiangent.cuacagempa.feature.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.actiangent.cuacagempa.core.common.result.Result
import com.actiangent.cuacagempa.core.data.repository.LocationWeatherRepository
import com.actiangent.cuacagempa.core.model.UserLocationWeather
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val compositeLocationWeatherRepository: LocationWeatherRepository,
) : ViewModel() {

    val uiState: StateFlow<WeatherUiState> =
        compositeLocationWeatherRepository.observeWeathers().map { result ->
            when (result) {
                Result.Loading -> {
                    WeatherUiState.Loading
                }
                is Result.Success -> {
                    WeatherUiState.Success(result.data)
                }
                is Result.Error -> {
                    WeatherUiState.Error(result.message)
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = WeatherUiState.Loading
        )

    fun refresh() {
        compositeLocationWeatherRepository.forceUpdate()
    }

}

sealed interface WeatherUiState {
    object Loading : WeatherUiState
    data class Success(
        val data: UserLocationWeather
    ) : WeatherUiState

    data class Error(val message: String) : WeatherUiState
}