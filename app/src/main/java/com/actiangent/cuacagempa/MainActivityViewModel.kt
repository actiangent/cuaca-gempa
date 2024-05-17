package com.actiangent.cuacagempa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.actiangent.cuacagempa.core.data.repository.location.LocationRepository
import com.actiangent.cuacagempa.core.data.repository.preferences.UserDataRepository
import com.actiangent.cuacagempa.core.model.UserData
import com.actiangent.cuacagempa.sync.work.manager.WorkManagerSyncManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    userDataRepository: UserDataRepository,
    private val syncManager: WorkManagerSyncManager
) : ViewModel() {

    private val _locationRequestError: MutableStateFlow<String?> = MutableStateFlow(null)
    private val _showPermissionDialog: MutableStateFlow<String?> = MutableStateFlow(null)

    init {
        syncManager.startEarthquakeSyncWork()
    }

    // TODO - refetch when userRegencyIds change, but not on first value emitted
    val uiState: StateFlow<MainActivityUiState> = userDataRepository.userData
        .map { userData ->
            MainActivityUiState.Success(
                userData = userData
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

    fun startWeatherSyncWork() {
        viewModelScope.launch {
            if (!locationRepository.isGpsEnabled()) {
                showLocationRequestError()
            } else {
                syncManager.startWeatherSyncWork()
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

    fun showLocationPermissionsRevokedError() {
        _locationRequestError.update { "Location permissions revoked" }
    }

    fun locationRequestErrorShown() {
        _locationRequestError.update { null }
    }
}

sealed interface MainActivityUiState {
    object Loading : MainActivityUiState
    data class Success(
        val userData: UserData,
    ) : MainActivityUiState
}

data class WeatherQuakeErrorState(
    val locationRequestError: String? = null,
    val showPermissionDialog: String? = null
)