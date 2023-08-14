package com.actiangent.cuacagempa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.actiangent.cuacagempa.core.data.repository.location.LocationRepository
import com.actiangent.cuacagempa.core.data.repository.preferences.UserDataRepository
import com.actiangent.cuacagempa.sync.work.manager.WorkManagerSyncManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface MainActivityUiState {
    object Loading : MainActivityUiState
    data class Success(
        val isLocationCached: Boolean = false
    ) : MainActivityUiState
}

data class WeatherQuakeErrorState(
    val locationRequestError: String? = null,
    val showPermissionDialog: String? = null
)

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    userDataRepository: UserDataRepository,
    private val syncManager: WorkManagerSyncManager
) : ViewModel() {
    private val _locationRequestError: MutableStateFlow<String?> = MutableStateFlow(null)
    private val _showPermissionDialog: MutableStateFlow<String?> = MutableStateFlow(null)

    val uiState: StateFlow<MainActivityUiState> = userDataRepository.userData.map {
        MainActivityUiState.Success(
            isLocationCached = it.isLocationCached
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = MainActivityUiState.Loading
    )

    val errorState: StateFlow<WeatherQuakeErrorState> =
        combine(
            _locationRequestError,
            _showPermissionDialog
        ) { locationRequestError, showPermissionDialog ->
            WeatherQuakeErrorState(
                locationRequestError,
                showPermissionDialog
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = WeatherQuakeErrorState()
        )

    fun requestUpdatedLocation() {
        viewModelScope.launch {
            if (!locationRepository.isGpsEnabled()) {
                showLocationRequestError()
            } else {
                syncManager.startLocationAndWeatherSyncWork()
            }
        }
    }

    fun showPermissionDialog() {
        _showPermissionDialog.update { "This app wont function properly without location access" }
    }

    fun permissionDialogShown() {
        _showPermissionDialog.update { null }
    }

    private fun showLocationRequestError() {
        _locationRequestError.update { "Please turn on location" }
    }

    fun locationRequestErrorShown() {
        _locationRequestError.update { null }
    }

}