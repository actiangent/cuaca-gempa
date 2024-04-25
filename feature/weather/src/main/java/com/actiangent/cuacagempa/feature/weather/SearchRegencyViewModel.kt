package com.actiangent.cuacagempa.feature.weather

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.actiangent.cuacagempa.core.common.result.Result
import com.actiangent.cuacagempa.core.common.result.asResult
import com.actiangent.cuacagempa.core.data.repository.RegencyRepository
import com.actiangent.cuacagempa.core.data.repository.preferences.UserDataRepository
import com.actiangent.cuacagempa.core.model.Regency
import com.actiangent.cuacagempa.core.model.SaveableRegency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchRegencyViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val regencyRepository: RegencyRepository,
    private val userDataRepository: UserDataRepository,
) : ViewModel() {

    val userRegencyUiState: StateFlow<UserRegencyUiState> = userDataRepository.userData
        .flatMapLatest { userData ->
            val userRegencyIds = userData.userRegencyIds
            regencyRepository.getRegencies(userRegencyIds)
        }
        .asResult()
        .map { result ->
            when (result) {
                Result.Loading -> UserRegencyUiState.Loading
                is Result.Success -> UserRegencyUiState.Success(
                    regencies = result.data
                )

                is Result.Error -> UserRegencyUiState.Error(message = result.message)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = UserRegencyUiState.Loading
        )

    val searchQuery = savedStateHandle.getStateFlow(key = SEARCH_QUERY, initialValue = "")

    val searchRegencyUiState: StateFlow<SearchRegencyUiState> = userDataRepository.userData
        .flatMapLatest { userData ->
            val userRegencyIds = userData.userRegencyIds
            searchQuery.flatMapLatest { query ->
                if (query.length < SEARCH_QUERY_MIN_LENGTH) {
                    flowOf(SearchRegencyUiState.EmptyQuery)
                } else {
                    regencyRepository.searchAllRegencies(query)
                        .map { regencies ->
                            regencies.map { regency ->
                                SaveableRegency(
                                    regency = regency,
                                    isSaved = regency.id in userRegencyIds
                                )
                            }
                        }
                        .map { regencies ->
                            SearchRegencyUiState.Success(regencies = regencies)
                        }
                        .catch {
                            SearchRegencyUiState.Error(message = it.message ?: "An error occurred")
                        }
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = SearchRegencyUiState.Loading
        )


    fun setSearchQuery(query: String) {
        savedStateHandle[SEARCH_QUERY] = query
    }

    fun setSelectedUserRegencyId(regencyId: String) {
        savedStateHandle["selectedUserRegencyId"] = regencyId
    }

    fun saveRegency(regencyId: String, saved: Boolean) {
        viewModelScope.launch {
            userDataRepository.setUserRegencyIdSaved(regencyId, saved)
        }
    }

}

sealed interface UserRegencyUiState {
    object Loading : UserRegencyUiState
    data class Success(
        val regencies: List<Regency>,
    ) : UserRegencyUiState {
        fun isEmpty(): Boolean = regencies.isEmpty()
    }

    data class Error(val message: String) : UserRegencyUiState
}

sealed interface SearchRegencyUiState {
    object Loading : SearchRegencyUiState
    object EmptyQuery : SearchRegencyUiState
    data class Success(
        val regencies: List<SaveableRegency>,
    ) : SearchRegencyUiState

    data class Error(val message: String) : SearchRegencyUiState
}

private const val SEARCH_QUERY_MIN_LENGTH = 2
private const val SEARCH_QUERY = "searchQuery"