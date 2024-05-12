package com.actiangent.cuacagempa.earthquake

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.actiangent.cuacagempa.core.common.result.Result
import com.actiangent.cuacagempa.core.data.repository.earthquake.EarthquakeRepository
import com.actiangent.cuacagempa.core.model.DetailedEarthquake
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class EarthquakeViewModel @Inject constructor(
    private val earthquakeRepository: EarthquakeRepository,
) : ViewModel() {

    val earthquakeUiState: StateFlow<EarthquakeUiState> = combine(
        earthquakeRepository.getRecentEarthquake(),
        earthquakeRepository.getLatestCautiousEarthquakes()
    ) { recentEarthquake, latestCautiousEarthquakes ->
        recentEarthquake to latestCautiousEarthquakes
    }
        .map { (recentEarthquakeResult, latestCautiousEarthquakesResult) ->
            when (latestCautiousEarthquakesResult) {
                Result.Loading -> EarthquakeUiState.Loading

                is Result.Success -> {
                    when (recentEarthquakeResult) {
                        Result.Loading -> EarthquakeUiState.Loading
                        is Result.Success -> {
                            EarthquakeUiState.Success(
                                recentEarthquake = recentEarthquakeResult.data,
                                latestCautiousEarthquakes = latestCautiousEarthquakesResult.data
                            )
                        }

                        is Result.Error -> EarthquakeUiState.Error(message = recentEarthquakeResult.message)
                    }
                }

                is Result.Error -> EarthquakeUiState.Error(message = latestCautiousEarthquakesResult.message)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = EarthquakeUiState.Loading
        )

}

sealed interface EarthquakeUiState {
    object Loading : EarthquakeUiState
    data class Success(
        val recentEarthquake: DetailedEarthquake,
        val latestCautiousEarthquakes: List<DetailedEarthquake>,
    ) : EarthquakeUiState

    data class Error(val message: String) : EarthquakeUiState
}