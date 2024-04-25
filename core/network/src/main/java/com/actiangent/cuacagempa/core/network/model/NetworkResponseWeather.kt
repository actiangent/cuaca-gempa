package com.actiangent.cuacagempa.core.network.model

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.TextContent
import com.tickaroo.tikxml.annotation.Xml
import kotlinx.datetime.Instant

@Xml(name = "data")
internal data class NetworkResponseWeatherData(
    @Element(name = "forecast") val forecast: NetworkResponseWeather,
)

@Xml(name = "forecast")
internal data class NetworkResponseWeather(
    @Element(name = "issue") val issue: NetworkResponseWeatherIssue,
    @Element(name = "area") val data: List<NetworkResponseWeatherArea>,
)

@Xml
internal data class NetworkResponseWeatherIssue(
    @PropertyElement(name = "timestamp") val datetime: Instant,
)

@Xml(name = "area")
internal data class NetworkResponseWeatherArea(
    @Attribute val id: String,
    @Attribute val latitude: Double,
    @Attribute val longitude: Double,
    @Attribute val description: String,
    @Attribute val type: String,
    @Element(name = "name") val names: List<NetworkResponseWeatherAreaName>,
    @Element(name = "parameter") val parameters: List<NetworkResponseWeatherParameter>?, // optional
)

@Xml(name = "name")
internal data class NetworkResponseWeatherAreaName(
    @Attribute(name = "xml:lang") val lang: String,
    @TextContent val value: String,
)

@Xml(name = "parameter")
internal data class NetworkResponseWeatherParameter(
    @Attribute val id: String,
    @Attribute val description: String,
    @Attribute val type: String,
    @Element(name = "timerange") val timeRanges: List<NetworkResponseWeatherTimeRange>,
)

@Xml(name = "timerange")
internal data class NetworkResponseWeatherTimeRange(
    @Attribute(name = "h") val hour: Int? = null, // optional
    @Attribute val day: String? = null, // optional
    @Attribute val datetime: Instant,
    @Element val values: List<NetworkResponseWeatherTimeRangeValue>,
)

@Xml(name = "value")
internal data class NetworkResponseWeatherTimeRangeValue(
    @Attribute(name = "unit") val unit: String,
    @TextContent val value: String,
)