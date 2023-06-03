package com.actiangent.cuacagempa.core.data.repository.preferences

import com.actiangent.cuacagempa.core.datastore.WeatherQuakePreferencesDataSource
import com.actiangent.cuacagempa.core.model.TemperatureOptions
import com.actiangent.cuacagempa.core.model.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultUserDataRepository @Inject constructor(
    private val weatherQuakePreferences: WeatherQuakePreferencesDataSource
) : UserDataRepository {

    override val userData: Flow<UserData> = weatherQuakePreferences.userData

    override suspend fun setLatitudeLongitude(latitude: Double, longitude: Double) {
        weatherQuakePreferences.setLatitudeLongitude(latitude, longitude)
    }

    override suspend fun setProvinceEndpoint(endpoint: String?) {
        endpoint?.let { weatherQuakePreferences.setProvinceEndpoint(it) }
    }

    override suspend fun setDistrictId(districtId: String) {
        weatherQuakePreferences.setDistrictId(districtId)
    }

    override suspend fun setTemperatureOption(option: TemperatureOptions) {
        weatherQuakePreferences.setTemperatureOption(option)
    }
}