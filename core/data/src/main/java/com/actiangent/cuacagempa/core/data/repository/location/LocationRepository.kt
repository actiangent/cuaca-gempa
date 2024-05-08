package com.actiangent.cuacagempa.core.data.repository.location

import android.location.Location

interface LocationRepository {

    suspend fun requestUpdatedLocation(): Location

    fun isGpsEnabled(): Boolean
}