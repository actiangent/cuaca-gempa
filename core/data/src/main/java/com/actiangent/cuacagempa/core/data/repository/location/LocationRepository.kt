package com.actiangent.cuacagempa.core.data.repository.location

import android.location.Location
import kotlinx.coroutines.CoroutineDispatcher

interface LocationRepository {

    suspend fun requestUpdatedLocation(): Location

    fun isGpsEnabled(): Boolean
}