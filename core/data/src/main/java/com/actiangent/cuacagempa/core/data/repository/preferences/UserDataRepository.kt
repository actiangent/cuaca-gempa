package com.actiangent.cuacagempa.core.data.repository.preferences

import com.actiangent.cuacagempa.core.model.TemperatureUnit
import com.actiangent.cuacagempa.core.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {

    val userData: Flow<UserData>

    suspend fun setUserRegencyIds(regencyIds: Set<String>)

    suspend fun setUserRegencyIdSaved(regencyId: String, saved: Boolean)

    suspend fun setTemperatureUnit(unit: TemperatureUnit)
}