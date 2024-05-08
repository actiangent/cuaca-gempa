package com.actiangent.cuacagempa.feature.weather

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.actiangent.cuacagempa.core.common.result.Result
import com.actiangent.cuacagempa.core.data.repository.RegencyRepository
import com.actiangent.cuacagempa.core.data.repository.UserDistrictWeatherRepository
import com.actiangent.cuacagempa.core.data.repository.preferences.UserDataRepository
import com.actiangent.cuacagempa.core.model.Forecast
import com.actiangent.cuacagempa.core.model.Regency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.takeWhile
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class UserRegencyForecastViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    userDataRepository: UserDataRepository,
    regencyRepository: RegencyRepository,
    userDistrictWeatherRepository: UserDistrictWeatherRepository
) : ViewModel() {

    private val userRegencyIds = userDataRepository.userData
        .map { userData -> userData.userRegencyIds }

    private val userRegencies = userRegencyIds
        .flatMapLatest { regencyIds ->
            regencyRepository.getRegencies(regencyIds)
        }

    val userRegenciesUiState: StateFlow<UserRegenciesUiState> = userRegencies
        .map { regencies ->
            UserRegenciesUiState.Success(
                regencies = regencies
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = UserRegenciesUiState.Loading
        )

    val selectedUserRegencyIndex =
        savedStateHandle.getStateFlow(key = "selectedUserRegencyId", initialValue = 0)

    val forecastsUiState: StateFlow<UserRegencyForecastsUiState> = userRegencies
        .map { regencies -> regencies.map(Regency::id) }
        .flatMapLatest { regencyIds ->
            selectedUserRegencyIndex
                .takeWhile { index ->
                    (index < regencyIds.size)
                        .also { isNotIndexOutOfBounds ->
                            if (!isNotIndexOutOfBounds) {
                                savedStateHandle["selectedUserRegencyId"] = (regencyIds.size - 1)
                            }
                        }
                }
                .flatMapLatest { index ->
                    if (regencyIds.isNotEmpty()) {
                        userDistrictWeatherRepository
                            .observeUserRegencyWeatherForecast(regencyIds.elementAt(index))
                    } else {
                        flowOf(Result.Loading)
                    }
                }
        }
        .map { result ->
            when (result) {
                Result.Loading -> UserRegencyForecastsUiState.Loading

                is Result.Success ->
                    UserRegencyForecastsUiState.Success(
                        forecasts = result.data
                    )

                is Result.Error -> UserRegencyForecastsUiState.Error(result.message)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = UserRegencyForecastsUiState.Loading
        )

    fun setSelectedUserRegencyIndex(index: Int) {
        savedStateHandle["selectedUserRegencyId"] = index
    }
}

sealed interface UserRegencyForecastsUiState {
    object Loading : UserRegencyForecastsUiState
    data class Success(
        val forecasts: List<Forecast>,
    ) : UserRegencyForecastsUiState

    data class Error(val message: String) : UserRegencyForecastsUiState
}

