package com.actiangent.cuacagempa.core.data.repository.location

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.location.Location
import com.actiangent.cuacagempa.core.location.LocationProvider
import com.actiangent.cuacagempa.core.location.getAddress
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asExecutor
import javax.inject.Inject

@SuppressLint("MissingPermission")
class DefaultLocationRepository @Inject constructor(
    private val locationProvider: LocationProvider,
    private val geocoder: Geocoder
) : LocationRepository {

    override suspend fun requestUpdatedLocation(dispatcher: CoroutineDispatcher): Location =
        locationProvider.awaitCurrentLocation(dispatcher.asExecutor())

    override suspend fun requestAddress(location: Location): Address? =
        geocoder.getAddress(location)

    override fun isGpsEnabled() = locationProvider.isGpsEnabled()
}