package com.actiangent.cuacagempa.feature.weather

import com.actiangent.cuacagempa.core.model.Regency

sealed interface UserRegenciesUiState {
    object Loading : UserRegenciesUiState
    data class Success(
        val regencies: List<Regency>,
    ) : UserRegenciesUiState
}