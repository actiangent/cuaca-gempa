package com.actiangent.cuacagempa.core.location

import android.location.Address
import android.location.Location
import kotlinx.coroutines.CoroutineDispatcher

interface LocationDataSource {

    suspend fun requestUpdatedLocation(dispatcher: CoroutineDispatcher): Location

    suspend fun requestLastLocation(dispatcher: CoroutineDispatcher): Location

    suspend fun requestAddress(location: Location): Address?

    fun isGpsEnabled(): Boolean
}