package com.actiangent.cuacagempa.core.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import com.actiangent.cuacagempa.core.model.TemperatureUnit
import com.actiangent.cuacagempa.core.model.UserData
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.io.IOException
import javax.inject.Inject

class WeatherQuakePreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>,
) {

    val userData = userPreferences.data
        .map { userPreferences ->
            UserData(
                userRegencyIds = userPreferences.userRegencyIdsMap.keys,
                temperatureUnit = when (userPreferences.temperatureUnit) {
                    null,
                    TemperatureUnitsProto.UNRECOGNIZED,
                    TemperatureUnitsProto.TEMPERATURE_CELSIUS -> TemperatureUnit.CELSIUS

                    TemperatureUnitsProto.TEMPERATURE_FAHRENHEIT -> TemperatureUnit.FAHRENHEIT
                },
                hasDoneOnboarding = true
            )
        }
        .distinctUntilChanged()
        .onEach { Log.d("userData", ": $it") }

    suspend fun setUserRegencyIds(regencyIds: Set<String>) {
        try {
            userPreferences.updateData {
                it.copy {
                    userRegencyIds.clear()
                    userRegencyIds.putAll(regencyIds.associateWith { true })
                    updateHasDoneOnboardingIfNecessary()
                }
            }
        } catch (ioException: IOException) {
            Log.e("WeatherQuakePreferences", "Failed to update user preferences", ioException)
        }
    }

    suspend fun setUserRegencyIdSaved(regencyId: String, saved: Boolean) {
        try {
            userPreferences.updateData {
                it.copy {
                    if (saved) {
                        userRegencyIds.put(regencyId, true)
                    } else {
                        userRegencyIds.remove(regencyId)
                    }
                    updateHasDoneOnboardingIfNecessary()
                }
            }
        } catch (ioException: IOException) {
            Log.e("WeatherQuakePreferences", "Failed to update user preferences", ioException)
        }
    }

    suspend fun setTemperatureUnit(unit: TemperatureUnit) {
        try {
            userPreferences.updateData {
                it.copy {
                    this.temperatureUnit = when (unit) {
                        TemperatureUnit.CELSIUS -> TemperatureUnitsProto.TEMPERATURE_CELSIUS
                        TemperatureUnit.FAHRENHEIT -> TemperatureUnitsProto.TEMPERATURE_FAHRENHEIT
                    }
                }
            }
        } catch (ioException: IOException) {
            Log.e("WeatherQuakePreferences", "Failed to update user preferences", ioException)
        }
    }

    suspend fun setHasDoneOnboarding(hasDoneOnboarding: Boolean) {
        userPreferences.updateData {
            it.copy { this.hasDoneOnboarding = hasDoneOnboarding }
        }
    }

}

private fun UserPreferencesKt.Dsl.updateHasDoneOnboardingIfNecessary() {
    if (userRegencyIds.isEmpty()) {
        hasDoneOnboarding = false
    }
}