package com.actiangent.cuacagempa.core.model

data class UserData(
    val userRegencyIds: Set<String>,
    val temperatureUnit: TemperatureUnit,
    val hasDoneOnboarding: Boolean,
)
