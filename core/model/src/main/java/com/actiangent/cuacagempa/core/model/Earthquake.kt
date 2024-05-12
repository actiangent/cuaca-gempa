package com.actiangent.cuacagempa.core.model

import kotlinx.datetime.LocalDateTime

data class Earthquake(
    val dateTime: LocalDateTime,
    val latitude: Double,
    val longitude: Double,
    val magnitude: Double,
    val depth: Int,
    val shakemapEndpoint: String? = null,
)