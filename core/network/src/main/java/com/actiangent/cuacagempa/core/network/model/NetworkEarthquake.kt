package com.actiangent.cuacagempa.core.network.model

import kotlinx.datetime.Instant

data class NetworkEarthquake(
    val dateTime: Instant,
    val latitude: Double,
    val longitude: Double,
    val magnitude: Double,
    val depth: Int,
    val shakemapEndpoint: String? = null,
)