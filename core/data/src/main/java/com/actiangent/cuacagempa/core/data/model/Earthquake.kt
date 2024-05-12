package com.actiangent.cuacagempa.core.data.model

import com.actiangent.cuacagempa.core.database.model.EarthquakeEntity
import com.actiangent.cuacagempa.core.network.model.NetworkEarthquake

fun NetworkEarthquake.asEarthquakeEntity() = EarthquakeEntity(
    dateTime = dateTime,
    latitude = latitude,
    longitude = longitude,
    magnitude = magnitude,
    depth = depth,
    shakemapEndpoint = shakemapEndpoint,
)