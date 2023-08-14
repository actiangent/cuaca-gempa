package com.actiangent.cuacagempa.feature.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.actiangent.cuacagempa.core.common.result.Result
import com.actiangent.cuacagempa.core.data.repository.DistrictWeatherRepository
import com.actiangent.cuacagempa.core.model.Weather
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val districtWeatherRepository: DistrictWeatherRepository
) : ViewModel() {

    private val forceRefresh = MutableStateFlow(false)

    val uiState: StateFlow<WeatherUiState> = districtWeatherRepository.observeDistrictWeather()
        .map { weathersResult ->
            when (weathersResult) {
                Result.Loading -> {
                    WeatherUiState.Loading
                }
                is Result.Success -> {
                    WeatherUiState.Success(
                        placeName = "Place Name",
                        weathers = weathersResult.data
                    )
                }
                is Result.Error -> {
                    WeatherUiState.Error(weathersResult.message)
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = WeatherUiState.Loading
        )

    fun refresh() {
        forceRefresh.update { true }
    }

}

sealed interface WeatherUiState {
    object Loading : WeatherUiState
    data class Success(
        val placeName: String,
        val weathers: List<Weather>,
    ) : WeatherUiState

    data class Error(val message: String) : WeatherUiState
}