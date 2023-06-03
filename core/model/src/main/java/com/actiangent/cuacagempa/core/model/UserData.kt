package com.actiangent.cuacagempa.core.model

data class UserData(
    val latitude: Double,
    val longitude: Double,
    val provinceEndpoint: String,
    val districtId: String,
    val temperatureOption: TemperatureOptions
) {
    val isLocationCached =
        (latitude != 0.0) and (longitude != 0.0) and (provinceEndpoint.isNotEmpty())
}
