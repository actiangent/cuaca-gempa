package com.actiangent.cuacagempa.core.network.model

import com.tickaroo.tikxml.annotation.*
import kotlinx.datetime.Instant

@Xml(name = "data")
data class NetworkWeatherData(
    @Element(name = "forecast") val forecast: NetworkWeather,
)

@Xml(name = "forecast")
data class NetworkWeather(
    @Element(name = "issue") val issue: NetworkWeatherIssue,
    @Element(name = "area") val data: List<NetworkWeatherArea>,
)

@Xml
data class NetworkWeatherIssue(
    @PropertyElement(name = "timestamp") val datetime: Instant,
)

@Xml(name = "area")
data class NetworkWeatherArea(
    @Attribute val id: String,
    @Attribute val latitude: Double,
    @Attribute val longitude: Double,
    @Attribute val description: String,
    @Attribute val domain: String,
    @Element(name = "name") val names: List<NetworkWeatherAreaName>,
    @Element(name = "parameter") val parameters: List<NetworkWeatherParameter>?, // optional
)

@Xml(name = "name")
data class NetworkWeatherAreaName(
    @Attribute(name = "xml:lang") val lang: String,
    @TextContent val value: String,
)

@Xml(name = "parameter")
data class NetworkWeatherParameter(
    @Attribute val id: String,
    @Attribute val description: String,
    @Attribute val type: String,
    @Element(name = "timerange") val timeRanges: List<NetworkWeatherTimeRange>,
)

@Xml(name = "timerange")
data class NetworkWeatherTimeRange(
    @Attribute(name = "h") val hour: Int? = null, // optional
    @Attribute val day: String? = null, // optional
    @Attribute val datetime: Instant,
    @Element val values: List<NetworkWeatherTimeRangeValue>,
)

@Xml(name = "value")
data class NetworkWeatherTimeRangeValue(
    @Attribute(name = "unit") val unit: String,
    @TextContent val value: String,
)