package com.actiangent.cuacagempa.feature.weather.weather

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.actiangent.cuacagempa.core.common.result.Result
import com.actiangent.cuacagempa.core.common.result.asResult
import com.actiangent.cuacagempa.core.data.repository.RegencyRepository
import com.actiangent.cuacagempa.core.data.repository.UserDistrictWeatherRepository
import com.actiangent.cuacagempa.core.data.repository.preferences.UserDataRepository
import com.actiangent.cuacagempa.core.model.Regency
import com.actiangent.cuacagempa.core.model.RegencyForecasts
import com.actiangent.cuacagempa.feature.weather.WeatherUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    userDataRepository: UserDataRepository,
    regencyRepository: RegencyRepository,
    userDistrictWeatherRepository: UserDistrictWeatherRepository
) : ViewModel() {

    val userRegencies = userDataRepository.userData
        .flatMapLatest { userData ->
            regencyRepository.getRegencies(userData.userRegencyIds)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    val selectedUserRegencyIndex =
        savedStateHandle.getStateFlow(key = "selectedUserRegencyId", initialValue = 0)

    val uiState: StateFlow<WeatherUiState> = userDataRepository.userData
        .flatMapLatest { userData ->
            val userRegencyIds = userData.userRegencyIds
            selectedUserRegencyIndex.flatMapLatest { index ->
                Log.d("index", ": $index")
                if (index >= userRegencyIds.size && userRegencyIds.isNotEmpty()) {
                    userDistrictWeatherRepository.observeUserRegencyWeatherForecast(
                        userRegencyIds.elementAt(userRegencyIds.size - 1)
                    )
                } else if (userRegencyIds.isNotEmpty()) {
                    userDistrictWeatherRepository.observeUserRegencyWeatherForecast(
                        userRegencyIds.elementAt(index)
                    )
                } else {
                    flowOf(
                        RegencyForecasts(
                            regency = Regency.empty(),
                            forecasts = emptyList()
                        )
                    ).asResult()
                }
            }
        }
        .map { result ->
            when (result) {
                Result.Loading -> {
                    WeatherUiState.Loading
                }

                is Result.Success -> {
                    WeatherUiState.Success(
                        data = result.data
                    )
                }

                is Result.Error -> {
                    WeatherUiState.Error(result.message)
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = WeatherUiState.Loading
        )

    fun setSelectedUserRegencyIndex(index: Int) {
        savedStateHandle["selectedUserRegencyId"] = index
    }
}

enum class WeathersFilter {
    SIXHOURS, DAILY
}