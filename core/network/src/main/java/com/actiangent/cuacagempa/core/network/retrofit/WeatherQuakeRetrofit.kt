package com.actiangent.cuacagempa.core.network.retrofit

import com.actiangent.cuacagempa.core.common.WrongEndpointException
import com.actiangent.cuacagempa.core.network.RemoteWeatherDataSource
import com.actiangent.cuacagempa.core.network.model.NetworkWeatherData
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Inject
import javax.inject.Singleton

const val baseUrl = "https://data.bmkg.go.id/DataMKG/"

private interface WeatherApi {

    @GET("MEWS/DigitalForecast/DigitalForecast-{cityName}.xml")
    suspend fun getProvinceWeather(@Path("cityName") province: String): NetworkWeatherData

}

private interface EarthQuakeApi

@Singleton
class WeatherRetrofit @Inject constructor(
    retrofit: Retrofit
) : RemoteWeatherDataSource {

    private val weatherApi = retrofit.create(WeatherApi::class.java)

    override suspend fun getProvinceWeather(province: String): NetworkWeatherData =
        weatherApi.getProvinceWeather(province.decideProvinceEndpoint())

    private fun String.decideProvinceEndpoint(): String = when {
        this.contains("Aceh", false) -> "Aceh"
        this.contains("Bali", false) -> "Bali"
        this.contains("Bangka Belitung", false) -> "BangkaBelitung"
        this.contains("Banten", false) -> "Banten"
        this.contains("Bengkulu", false) -> "Bengkulu"
        this.contains("Yogyakarta", false) -> "DIYogyakarta"
        this.contains("Jakarta", false) -> "DKIJakarta"
        this.contains("Gorontalo", false) -> "Gorontalo"
        this.contains("Jambi", false) -> "Jambi"
        this.contains("Jawa Barat", false) -> "JawaBarat"
        this.contains("Jawa Tengah", false) -> "JawaTengah"
        this.contains("Jawa Timur", false) -> "JawaTimur"
        this.contains("Kalimantan Barat", false) -> "KalimantanBarat"
        this.contains("Kalimantan Selatan", false) -> "KalimantanSelatan"
        this.contains("Kalimantan Tengah", false) -> "KalimantanTengah"
        this.contains("Kalimantan Timur", false) -> "KalimantanTimur"
        this.contains("Kalimantan Utara", false) -> "KalimantanUtara"
        this.contains("Kepulauan Riau", false) -> "KepulauanRiau"
        this.contains("Lampung", false) -> "Lampung"
        this.contains("Maluku Utara", false) -> "MalukuUtara"
        this.contains("Nusa Tenggara Barat", false) -> "NusaTenggaraBarat"
        this.contains("Nusa Tenggara Timur", false) -> "NusaTenggaraTimur"
        this.contains("Papua Barat", false) -> "PapuaBarat"
        this.contains("Sulawesi Barat", false) -> "SulawesiBarat"
        this.contains("Sulawesi Selatan", false) -> "SulawesiSelatan"
        this.contains("Sulawesi Tengah", false) -> "SulawesiTengah"
        this.contains("Sulawesi Tenggara", false) -> "SulawesiTenggara"
        this.contains("Sulawesi Utara", false) -> "SulawesiUtara"
        this.contains("Sumatera Barat", false) -> "SumateraBarat"
        this.contains("Sumatera Selatan", false) -> "SumateraSelatan"
        this.contains("Sumatera Utara", false) -> "SumateraUtara"
        this.contains("Maluku", false) -> "Maluku"
        this.contains("Papua", false) -> "Papua"
        this.contains("Riau", false) -> "Riau"
        else -> throw WrongEndpointException()
    }

}