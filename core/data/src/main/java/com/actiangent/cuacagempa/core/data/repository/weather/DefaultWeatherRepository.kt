package com.actiangent.cuacagempa.core.data.repository.weather

import com.actiangent.cuacagempa.core.data.model.asEntities
import com.actiangent.cuacagempa.core.database.dao.WeatherDao
import com.actiangent.cuacagempa.core.location.locationFromLatLon
import com.actiangent.cuacagempa.core.model.Weather
import com.actiangent.cuacagempa.core.network.RemoteWeatherDataSource
import com.actiangent.cuacagempa.core.network.model.NetworkWeatherArea
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultWeatherRepository @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val remoteDataSource: RemoteWeatherDataSource,
    private val dao: WeatherDao
) : WeatherRepository {

    override suspend fun getCurrentWeather(
        districtIds: Set<String>,
        temperaturePreference: String
    ): Flow<List<Weather>> = dao.getCurrentWeathers(districtIds, temperaturePreference)

    override suspend fun fetchUserCurrentWeather(
        latitude: Double,
        longitude: Double,
        province: String,
        saveCurrentAreaId: suspend (String) -> Unit
    ) = withContext(ioDispatcher) {
        remoteDataSource.getProvinceWeather(province)
            .run {
                val (district, weathers) = nearestDistrict(forecast.data, latitude, longitude)
                    .asEntities()
                dao.insertDistrictWeathers(district, weathers)
                saveCurrentAreaId(district.districtId)
            }
    }

    override suspend fun fetchIndonesiaCurrentWeather() {
        TODO("Not yet implemented")
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