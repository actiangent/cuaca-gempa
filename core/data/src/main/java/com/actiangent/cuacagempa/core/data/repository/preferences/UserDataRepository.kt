package com.actiangent.cuacagempa.core.data.repository.preferences

import com.actiangent.cuacagempa.core.data.model.UserLocationData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {

    val userLocationData: Flow<UserLocationData>

    val districtId: Flow<String>

    suspend fun setLocationCoordinate(coordinate: Pair<Double, Double>)

    suspend fun setLocationProvince(province: String?)

    suspend fun setDistrictId(districtId: String)

}