package com.actiangent.cuacagempa.core.data.repository.location

import android.location.Address
import android.location.Location
import com.actiangent.cuacagempa.core.common.dispatcher.Dispatcher
import kotlinx.coroutines.CoroutineDispatcher
import java.util.concurrent.Executor

interface LocationRepository {
    suspend fun requestUpdatedLocation(dispatcher: CoroutineDispatcher): Location

    suspend fun requestAddress(location: Location): Address?

    fun isGpsEnabled(): Boolean
}