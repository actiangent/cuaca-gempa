package com.actiangent.cuacagempa.core.model

import kotlinx.datetime.LocalDateTime

data class Weather(
    val timestamp: LocalDateTime,
    val code: Int,
    val temperature: Double,
    val humidity: Int,
)
