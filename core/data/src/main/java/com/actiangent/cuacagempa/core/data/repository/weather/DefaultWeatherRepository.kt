package com.actiangent.cuacagempa.core.data.repository.weather

import com.actiangent.cuacagempa.core.data.model.asEntities
import com.actiangent.cuacagempa.core.database.dao.WeatherDao
import com.actiangent.cuacagempa.core.database.model.DistrictEntity
import com.actiangent.cuacagempa.core.database.model.WeatherEntity
import com.actiangent.cuacagempa.core.location.locationFromLatLon
import com.actiangent.cuacagempa.core.model.Weather
import com.actiangent.cuacagempa.core.network.RemoteWeatherDataSource
import com.actiangent.cuacagempa.core.network.model.NetworkWeatherArea
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultWeatherRepository @Inject constructor(
    private val remoteDataSource: RemoteWeatherDataSource,
    private val dao: WeatherDao
) : WeatherRepository {

    override suspend fun getLocalCurrentDistrictWeathers(
        districtId: String,
        temperaturePreferences: String
    ): List<Weather> = dao.getCurrentDistrictWeathers(districtId, temperaturePreferences)

    override suspend fun fetchCurrentDistrictWeathers(
        lat: Double,
        lon: Double,
        province: String
    ) = withContext(Dispatchers.IO) {
        val response = remoteDataSource.getProvinceWeather(province)
        val networkWeatherArea = nearestDistrict(response.forecast.data, lat, lon)
        return@withContext networkWeatherArea.asEntities()
    }

    override suspend fun insertCurrentDistrictWeathers(
        entities: Pair<DistrictEntity, List<WeatherEntity>>
    ) {
        val (district, weathers) = entities
        dao.insertDistrictWeathers(
            district = district,
            weathers = weathers
        )
    }

    private fun nearestDistrict(
        districts: List<NetworkWeatherArea>,
        lat: Double,
        lon: Double
    ): NetworkWeatherArea {
        val distances = mutableListOf<Float>()
        for (district in districts) {
            if (district.parameters == null) continue

            val areaLocation = locationFromLatLon(district.latitude, district.longitude)
            val loc = locationFromLatLon(lat, lon)
            distances.add(areaLocation.distanceTo(loc))
        }
        val nearestIndex = distances.withIndex().minBy { it.value }.index
        return districts[nearestIndex]
    }

}