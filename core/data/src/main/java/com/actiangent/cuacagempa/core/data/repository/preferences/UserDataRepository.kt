package com.actiangent.cuacagempa.core.data.repository.preferences

import com.actiangent.cuacagempa.core.model.TemperatureOptions
import com.actiangent.cuacagempa.core.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {

    val userData: Flow<UserData>

    suspend fun setLatitudeLongitude(latitude: Double, longitude: Double)

    suspend fun setProvinceEndpoint(endpoint: String?)

    suspend fun setDistrictId(districtId: String)

    suspend fun setTemperatureOption(options: TemperatureOptions)

}