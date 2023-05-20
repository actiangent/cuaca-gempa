package com.actiangent.cuacagempa.core.data.repository.preferences

import com.actiangent.cuacagempa.core.data.model.UserLocationData
import com.actiangent.cuacagempa.core.data.preferences.WeatherQuakeDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultUserDataRepository @Inject constructor(
    private val weatherQuakeDataStore: WeatherQuakeDataStore
) : UserDataRepository {

    override val userLocationData: Flow<UserLocationData> = weatherQuakeDataStore.userLocationData

    override val districtId: Flow<String> = weatherQuakeDataStore.districtId

    override suspend fun setLocationCoordinate(coordinate: Pair<Double, Double>) {
        weatherQuakeDataStore.setLocationCoordinate(coordinate)
    }

    override suspend fun setLocationProvince(province: String?) {
        weatherQuakeDataStore.setProvinceName(province)
    }

    override suspend fun setDistrictId(districtId: String) {
        weatherQuakeDataStore.setDistrictId(districtId)
    }
}