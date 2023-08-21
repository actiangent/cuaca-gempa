package com.actiangent.cuacagempa.sync.work.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.actiangent.cuacagempa.core.common.dispatcher.Dispatcher
import com.actiangent.cuacagempa.core.common.dispatcher.WeatherQuakeDispatchers
import com.actiangent.cuacagempa.core.data.repository.location.LocationRepository
import com.actiangent.cuacagempa.core.data.repository.preferences.UserDataRepository
import com.actiangent.cuacagempa.sync.work.SyncConstraints
import com.actiangent.cuacagempa.sync.work.WorkDataKeys.WEATHER_SYNC_LATITUDE_KEY
import com.actiangent.cuacagempa.sync.work.WorkDataKeys.WEATHER_SYNC_LONGITUDE_KEY
import com.actiangent.cuacagempa.sync.work.WorkDataKeys.WEATHER_SYNC_PROVINCE_KEY
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

@HiltWorker
class LocationSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    @Dispatcher(WeatherQuakeDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val locationRepository: LocationRepository,
    private val userDataRepository: UserDataRepository,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(ioDispatcher) {
        val location = locationRepository.requestUpdatedLocation(ioDispatcher)
        val address = locationRepository.requestAddress(location)
        userDataRepository.setLatitudeLongitude(location.latitude, location.longitude)
        userDataRepository.setProvince(address?.adminArea)

        val outputData = workDataOf(
            WEATHER_SYNC_LATITUDE_KEY to location.latitude,
            WEATHER_SYNC_LONGITUDE_KEY to location.longitude,
            WEATHER_SYNC_PROVINCE_KEY to address?.adminArea,
        )

        Result.success(outputData)
    }

    companion object {

        fun startUpRequestLocationWork() = OneTimeWorkRequestBuilder<LocationSyncWorker>()
            .setConstraints(SyncConstraints)
            .build()
    }
}