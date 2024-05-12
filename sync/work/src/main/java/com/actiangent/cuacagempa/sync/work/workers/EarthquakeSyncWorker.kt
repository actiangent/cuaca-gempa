package com.actiangent.cuacagempa.sync.work.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import com.actiangent.cuacagempa.core.common.dispatcher.Dispatcher
import com.actiangent.cuacagempa.core.common.dispatcher.WeatherQuakeDispatchers
import com.actiangent.cuacagempa.core.data.EarthquakeSynchronizer
import com.actiangent.cuacagempa.core.data.repository.earthquake.EarthquakeRepository
import com.actiangent.cuacagempa.sync.work.SyncConstraints
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

@HiltWorker
class EarthquakeSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    @Dispatcher(WeatherQuakeDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val earthquakeRepository: EarthquakeRepository,
) : CoroutineWorker(appContext, workerParams), EarthquakeSynchronizer {

    override suspend fun doWork(): Result = withContext(ioDispatcher) {

        earthquakeRepository.sync()

        Result.success()
    }

    companion object {

        fun startUpEarthquakeSyncWork() = OneTimeWorkRequestBuilder<EarthquakeSyncWorker>()
            .setConstraints(SyncConstraints)
            .build()
    }
}