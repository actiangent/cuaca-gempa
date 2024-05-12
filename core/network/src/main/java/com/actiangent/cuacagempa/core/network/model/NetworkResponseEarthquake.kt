package com.actiangent.cuacagempa.core.network.model

import com.google.gson.annotations.SerializedName
import kotlinx.datetime.Instant

data class NetworkResponseEarthquakeInfo <T> (

    @SerializedName("Infogempa")
    val data: T,
)

data class NetworkResponseEarthquakeData(

    @SerializedName("gempa")
    val earthquake: NetworkResponseEarthquake,
)

data class NetworkResponseEarthquakeListData(

    @SerializedName("gempa")
    val earthquake: List<NetworkResponseEarthquake>,
)

data class NetworkResponseEarthquake(

    @SerializedName("Tanggal")
    val localDate: String,

    @SerializedName("Jam")
    val localTime: String,

    @SerializedName("DateTime")
    val dateTime: Instant,

    @SerializedName("Coordinates")
    val latLon: String,

    @SerializedName("Lintang")
    val lat: String,

    @SerializedName("Bujur")
    val lon: String,

    @SerializedName("Magnitude")
    val magnitude: String,

    @SerializedName("Kedalaman")
    val depth: String,

    @SerializedName("Wilayah")
    val area: String,

    @SerializedName("Potensi")
    val tsunami: String,

    @SerializedName("Dirasakan")
    val felt: String?,

    @SerializedName("Shakemap")
    val shakemapEndpoint: String?,
)

