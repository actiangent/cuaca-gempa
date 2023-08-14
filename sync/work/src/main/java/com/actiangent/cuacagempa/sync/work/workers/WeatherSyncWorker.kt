package com.actiangent.cuacagempa.sync.work.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.actiangent.cuacagempa.core.common.dispatcher.Dispatcher
import com.actiangent.cuacagempa.core.common.dispatcher.WeatherQuakeDispatchers
import com.actiangent.cuacagempa.core.data.repository.preferences.UserDataRepository
import com.actiangent.cuacagempa.core.data.repository.weather.WeatherRepository
import com.actiangent.cuacagempa.sync.work.SyncConstraints
import com.actiangent.cuacagempa.sync.work.WorkDataKeys.WEATHER_SYNC_LATITUDE_KEY
import com.actiangent.cuacagempa.sync.work.WorkDataKeys.WEATHER_SYNC_LONGITUDE_KEY
import com.actiangent.cuacagempa.sync.work.WorkDataKeys.WEATHER_SYNC_PROVINCE_KEY
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

@HiltWorker
class WeatherSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    @Dispatcher(WeatherQuakeDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val userDataRepository: UserDataRepository,
    private val weatherRepository: WeatherRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(ioDispatcher) {
        val latitude = inputData.getDouble(WEATHER_SYNC_LATITUDE_KEY, 0.0)
        val longitude = inputData.getDouble(WEATHER_SYNC_LONGITUDE_KEY, 0.0)
        val province = inputData.getString(WEATHER_SYNC_PROVINCE_KEY)

        if (!province.isNullOrBlank()) {
            weatherRepository.fetchUserCurrentWeather(
                latitude,
                longitude,
                province,
            ) { areaId ->
                userDataRepository.setAreaId(areaId)
            }

            Result.success()
        } else {
            Result.failure()
        }
    }

    companion object {

        fun startUpWeatherSyncWork() = OneTimeWorkRequestBuilder<WeatherSyncWorker>()
            .setConstraints(SyncConstraints)
            .build()
    }
}