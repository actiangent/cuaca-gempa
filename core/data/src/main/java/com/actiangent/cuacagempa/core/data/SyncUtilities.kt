package com.actiangent.cuacagempa.core.data

import com.actiangent.cuacagempa.core.network.model.NetworkEarthquake
import com.actiangent.cuacagempa.core.network.model.NetworkRegencyWeather
import kotlinx.datetime.DateTimePeriod

interface WeatherSyncable {

    suspend fun syncByRegencyIdWith(regencyId: String, synchronizer: WeatherSynchronizer)

    suspend fun syncByLocationWith(synchronizer: WeatherSynchronizer)
}

interface WeatherSynchronizer {

    suspend fun WeatherSyncable.syncByRegencyId(regencyId: String) =
        this@syncByRegencyId.syncByRegencyIdWith(regencyId, this@WeatherSynchronizer)

    suspend fun WeatherSyncable.syncByLocation() =
        this@syncByLocation.syncByLocationWith(this@WeatherSynchronizer)

    suspend fun addUserRegencyId(regencyId: String)
}

// Cache expiration period, default to 3 days
private val weatherExpirationPeriod = DateTimePeriod(days = 3)

suspend fun WeatherSynchronizer.changeRegencyWeatherSync(
    weatherFetcher: suspend () -> NetworkRegencyWeather,
    // expirationPeriod: DateTimePeriod = weatherExpirationPeriod,
    modelInserter: suspend (NetworkRegencyWeather) -> Unit,
    modelDeleter: suspend () -> Unit,
    shouldSaveRegencyId: Boolean,
) {
    val data = weatherFetcher()
    modelInserter(data)

    if (shouldSaveRegencyId) {
        val regencyId = data.id
        addUserRegencyId(regencyId)
    }

    modelDeleter()
}

interface EarthquakeSyncable {

    suspend fun syncWith(synchronizer: EarthquakeSynchronizer)
}

interface EarthquakeSynchronizer {

    suspend fun EarthquakeSyncable.sync() =
        this@sync.syncWith(this@EarthquakeSynchronizer)
}

suspend fun EarthquakeSynchronizer.changeEarthquakeSync(
    earthquakeFetcher: suspend () -> List<NetworkEarthquake>,
    modelInserter: suspend (List<NetworkEarthquake>) -> Unit,
    modelDeleter: suspend () -> Unit,
) {
    val data = earthquakeFetcher()
    modelInserter(data)

    modelDeleter()
}



