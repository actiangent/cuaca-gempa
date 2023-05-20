package com.actiangent.cuacagempa.ui

import androidx.lifecycle.ViewModel
import com.actiangent.cuacagempa.core.data.repository.location.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    locationRepository: LocationRepository
) : ViewModel()