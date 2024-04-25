package com.actiangent.cuacagempa.sync.work

import androidx.work.Constraints
import androidx.work.NetworkType

val SyncConstraints
    get() = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

const val WEATHER_SYNC_WORK_NAME = "weatherSyncWork"
