package com.actiangent.cuacagempa.core.network

import com.actiangent.cuacagempa.core.network.model.NetworkEarthquake

interface RemoteEarthquakeDataSource {

    suspend fun getRecentEarthQuake(): NetworkEarthquake

    suspend fun getLatestCautiousEarthquakes(): List<NetworkEarthquake>
}