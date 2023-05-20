package com.actiangent.cuacagempa.core.network

import com.actiangent.cuacagempa.core.network.model.NetworkWeatherData

interface RemoteWeatherDataSource {

    suspend fun getProvinceWeather(province: String): NetworkWeatherData

}