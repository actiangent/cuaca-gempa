package com.actiangent.cuacagempa.core.data.repository.preferences

import com.actiangent.cuacagempa.core.datastore.WeatherQuakePreferencesDataSource
import com.actiangent.cuacagempa.core.model.TemperatureUnit
import com.actiangent.cuacagempa.core.model.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultUserDataRepository @Inject constructor(
    private val weatherQuakePreferences: WeatherQuakePreferencesDataSource
) : UserDataRepository {

    override val userData: Flow<UserData> = weatherQuakePreferences.userData

    override suspend fun setUserRegencyIds(regencyIds: Set<String>) {
        weatherQuakePreferences.setUserRegencyIds(regencyIds)
    }

    override suspend fun setUserRegencyIdSaved(regencyId: String, saved: Boolean) {
        weatherQuakePreferences.setUserRegencyIdSaved(regencyId, saved)
    }

    override suspend fun setTemperatureUnit(unit: TemperatureUnit) {
        weatherQuakePreferences.setTemperatureUnit(unit)
    }
}