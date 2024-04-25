package com.actiangent.cuacagempa.core.location

import android.location.Address
import android.location.Geocoder
import android.location.Location
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asExecutor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceLocationDataSource @Inject constructor(
    private val locationProvider: LocationProvider,
    private val geocoder: Geocoder
) : LocationDataSource {

    override suspend fun requestUpdatedLocation(dispatcher: CoroutineDispatcher): Location =
        locationProvider.awaitCurrentLocation(dispatcher.asExecutor())

    override suspend fun requestLastLocation(dispatcher: CoroutineDispatcher): Location {
        TODO("Not yet implemented")
    }

    override suspend fun requestAddress(location: Location): Address? =
        geocoder.getAddress(location)

    override fun isGpsEnabled(): Boolean = locationProvider.isGpsEnabled()
}