package com.actiangent.cuacagempa.core.data.model

data class UserLocationData(
    val latitude: Double? = null,
    val longitude: Double? = null,
    val province: String? = null,
) {
    val isLocationCached =
        (latitude != null) and (longitude != null) and (!province.isNullOrEmpty())
}
