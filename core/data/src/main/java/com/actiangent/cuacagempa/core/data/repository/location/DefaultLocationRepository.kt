package com.actiangent.cuacagempa.core.data.repository.location

import android.annotation.SuppressLint
import android.location.Location
import com.actiangent.cuacagempa.core.common.dispatcher.Dispatcher
import com.actiangent.cuacagempa.core.common.dispatcher.WeatherQuakeDispatchers.IO
import com.actiangent.cuacagempa.core.location.LocationProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asExecutor
import javax.inject.Inject

@SuppressLint("MissingPermission")
class DefaultLocationRepository @Inject constructor(
    private val locationProvider: LocationProvider,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
) : LocationRepository {

    override suspend fun requestUpdatedLocation(): Location =
        locationProvider.awaitCurrentLocation(ioDispatcher.asExecutor())

    override fun isGpsEnabled() = locationProvider.isGpsEnabled()
}