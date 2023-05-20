package com.actiangent.cuacagempa.core.data.repository.location

import android.location.Address
import android.location.Location

interface LocationRepository {

    suspend fun requestUpdatedLocation(): Location

    suspend fun requestAddress(location: Location): Address?

    fun isGpsEnabled(): Boolean

}