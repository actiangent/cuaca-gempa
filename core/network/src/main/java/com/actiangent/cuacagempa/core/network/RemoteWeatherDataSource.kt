package com.actiangent.cuacagempa.core.network

import com.actiangent.cuacagempa.core.network.model.NetworkRegencyWeather

interface RemoteWeatherDataSource {

    suspend fun getRegencyWeathers(
        provinceEndpoint: String,
        regencyId: String
    ): NetworkRegencyWeather
}