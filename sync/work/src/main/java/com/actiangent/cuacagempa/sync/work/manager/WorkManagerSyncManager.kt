package com.actiangent.cuacagempa.sync.work.manager

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.actiangent.cuacagempa.sync.work.LOCATION_SYNC_WORK_NAME
import com.actiangent.cuacagempa.sync.work.WEATHER_SYNC_WORK_NAME
import com.actiangent.cuacagempa.sync.work.workers.LocationSyncWorker
import com.actiangent.cuacagempa.sync.work.workers.WeatherSyncWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkManagerSyncManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val workManagerInstance get() = WorkManager.getInstance(context)

    fun startLocationSyncWork() {
        workManagerInstance.enqueueUniqueWork(
            LOCATION_SYNC_WORK_NAME,
            ExistingWorkPolicy.KEEP,
            LocationSyncWorker.startUpRequestLocationWork()
        )
    }

    fun startWeatherSyncWork() {
        workManagerInstance.enqueueUniqueWork(
            WEATHER_SYNC_WORK_NAME,
            ExistingWorkPolicy.KEEP,
            WeatherSyncWorker.startUpWeatherSyncWork()
        )
    }

    fun startLocationAndWeatherSyncWork() {
        workManagerInstance.beginUniqueWork(
            LOCATION_SYNC_WORK_NAME,
            ExistingWorkPolicy.KEEP,
            LocationSyncWorker.startUpRequestLocationWork()
        ).then(WeatherSyncWorker.startUpWeatherSyncWork())
            .enqueue()
    }

}

