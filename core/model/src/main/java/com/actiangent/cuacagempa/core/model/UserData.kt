package com.actiangent.cuacagempa.core.model

data class UserData(
    val latitude: Double,
    val longitude: Double,
    val province: String,
    val areaId: String,
    val temperatureOption: TemperatureOptions
) {
    val isLocationCached
        get() = (latitude != 0.0) and (longitude != 0.0) and (province.isNotEmpty())
}
