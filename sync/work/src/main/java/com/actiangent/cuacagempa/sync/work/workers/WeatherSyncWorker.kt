package com.actiangent.cuacagempa.sync.work.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import com.actiangent.cuacagempa.core.common.dispatcher.Dispatcher
import com.actiangent.cuacagempa.core.common.dispatcher.WeatherQuakeDispatchers
import com.actiangent.cuacagempa.core.data.WeatherSynchronizer
import com.actiangent.cuacagempa.core.data.repository.preferences.UserDataRepository
import com.actiangent.cuacagempa.core.data.repository.weather.WeatherRepository
import com.actiangent.cuacagempa.sync.work.SyncConstraints
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

@HiltWorker
class WeatherSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    @Dispatcher(WeatherQuakeDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val weatherRepository: WeatherRepository,
    private val userDataRepository: UserDataRepository
) : CoroutineWorker(appContext, workerParams), WeatherSynchronizer {

    override suspend fun doWork(): Result = withContext(ioDispatcher) {
        val userRegencyIds = userDataRepository.userData.first().userRegencyIds

        if (userRegencyIds.isEmpty()) {
            weatherRepository.syncByLocation()
        } else {
            userRegencyIds.map { regencyId ->
                async {
                    weatherRepository.syncByRegencyId(regencyId)
                }
            }.awaitAll()
        }

        Result.success()
    }

    override suspend fun addUserRegencyId(regencyId: String) {
        userDataRepository.setUserRegencyIdSaved(regencyId, true)
    }

    companion object {

        fun startUpWeatherSyncWork() = OneTimeWorkRequestBuilder<WeatherSyncWorker>()
            .setConstraints(SyncConstraints)
            .build()
    }
}