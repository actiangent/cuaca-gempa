package com.actiangent.cuacagempa.core.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import com.actiangent.cuacagempa.core.model.TemperatureOptions
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
                latitude = userPreferences.coordinate.latitude,
                longitude = userPreferences.coordinate.longitude,
                provinceEndpoint = userPreferences.provinceEndpoint,
                districtId = userPreferences.districtId,
                temperatureOption = when (userPreferences.temperatureOption) {
                    null,
                    TemperatureOptionsProto.UNRECOGNIZED,
                    TemperatureOptionsProto.TEMPERATURE_CELSIUS -> TemperatureOptions.CELSIUS
                    TemperatureOptionsProto.TEMPERATURE_FAHRENHEIT -> TemperatureOptions.FAHRENHEIT
                }
            )
        }.distinctUntilChanged()

    suspend fun setLatitudeLongitude(latitude: Double, longitude: Double) {
        try {
            Log.d("preferences", "setLatitudeLongitude: $latitude, $longitude")
            userPreferences.updateData {
                it.copy {
                    this.coordinate = this.coordinate.copy {
                        this.latitude = latitude
                        this.longitude = longitude
                    }
                }
            }
        } catch (ioException: IOException) {
            Log.e("WeatherQuakePreferences", "Failed to update user preferences", ioException)
        }
    }

    suspend fun setProvinceEndpoint(endpoint: String) {
        try {
            userPreferences.updateData {
                it.copy {
                    this.provinceEndpoint = endpoint
                }
            }
        } catch (ioException: IOException) {
            Log.e("WeatherQuakePreferences", "Failed to update user preferences", ioException)
        }
    }

    suspend fun setDistrictId(districtId: String) {
        try {
            Log.d("preferences", "setDistrictId: $districtId")
            userPreferences.updateData {
                if (it.districtId != districtId) {
                    it.copy {
                        this.districtId = districtId
                    }
                } else {
                    it
                }
            }
        } catch (ioException: IOException) {
            Log.e("WeatherQuakePreferences", "Failed to update user preferences", ioException)
        }
    }

    suspend fun setTemperatureOption(options: TemperatureOptions) {
        try {
            userPreferences.updateData {
                it.copy {
                    this.temperatureOption = when (options) {
                        TemperatureOptions.CELSIUS -> TemperatureOptionsProto.TEMPERATURE_CELSIUS
                        TemperatureOptions.FAHRENHEIT -> TemperatureOptionsProto.TEMPERATURE_FAHRENHEIT
                    }
                }
            }
        } catch (ioException: IOException) {
            Log.e("WeatherQuakePreferences", "Failed to update user preferences", ioException)
        }
    }

}