package com.actiangent.cuacagempa.sync.work

import androidx.work.Constraints
import androidx.work.NetworkType

val SyncConstraints
    get() = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

const val LOCATION_SYNC_WORK_NAME = "locationSyncWork"
const val WEATHER_SYNC_WORK_NAME = "weatherSyncWork"

object WorkDataKeys {
    const val WEATHER_SYNC_LATITUDE_KEY = "weather-sync-latitude-key"
    const val WEATHER_SYNC_LONGITUDE_KEY = "weather-sync-longitude-key"
    const val WEATHER_SYNC_PROVINCE_KEY = "weather-sync-province-key"
}