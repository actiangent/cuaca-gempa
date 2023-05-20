package com.actiangent.cuacagempa.feature.weather

import androidx.compose.ui.graphics.Color
import com.actiangent.cuacagempa.core.common.datetime.now
import com.actiangent.cuacagempa.core.common.datetime.toJakartaDateTime
import com.actiangent.cuacagempa.core.designsystem.icon.Icon
import com.actiangent.cuacagempa.core.designsystem.icon.WeatherQuakeIcons.Cloud
import com.actiangent.cuacagempa.core.designsystem.icon.WeatherQuakeIcons.CloudMoon
import com.actiangent.cuacagempa.core.designsystem.icon.WeatherQuakeIcons.CloudSun
import com.actiangent.cuacagempa.core.designsystem.icon.WeatherQuakeIcons.Clouds
import com.actiangent.cuacagempa.core.designsystem.icon.WeatherQuakeIcons.LightRain
import com.actiangent.cuacagempa.core.designsystem.icon.WeatherQuakeIcons.MoonStars
import com.actiangent.cuacagempa.core.designsystem.icon.WeatherQuakeIcons.Rain
import com.actiangent.cuacagempa.core.designsystem.icon.WeatherQuakeIcons.Sun
import com.actiangent.cuacagempa.core.designsystem.icon.WeatherQuakeIcons.Thunderstorm
import com.actiangent.cuacagempa.core.designsystem.theme.StormCloud
import com.actiangent.cuacagempa.core.designsystem.theme.VividSkyBlue
import com.actiangent.cuacagempa.core.designsystem.theme.WeldonBlue
import com.actiangent.cuacagempa.feature.weather.WeatherCode.*

enum class WeatherCode(
    val description: String
) {
    CLEAR_SKIES("Clear"),
    PARTLY_CLOUDY("Partly Cloudy"),
    MOSTLY_CLOUDY("Mostly Cloudy"),
    OVERCAST("Overcast"),
    HAZE("Haze"),
    SMOKE("Smoky"),
    FOG("Foggy"),
    LIGHT_RAIN("Light Rain"),
    RAIN("Rain"),
    HEAVY_RAIN("Heavy Rain"),
    ISOLATED_SHOWER("Isolated Rain"),
    SEVERE_THUNDERSTORM("Thunderstorm");
}

fun Int.asWeatherCode(): WeatherCode = when (this) {
    0 -> CLEAR_SKIES
    1, 2 -> PARTLY_CLOUDY
    3 -> MOSTLY_CLOUDY
    4 -> OVERCAST
    5 -> HAZE
    10 -> SMOKE
    45 -> FOG
    60 -> LIGHT_RAIN
    61 -> RAIN
    63 -> HEAVY_RAIN
    80 -> ISOLATED_SHOWER
    95, 97 -> SEVERE_THUNDERSTORM
    else -> CLEAR_SKIES
}

fun WeatherCode.asIcon(): Icon = when (this) {
    CLEAR_SKIES -> {
        val hour = now().toJakartaDateTime().hour
        if (hour in 18..24) {
            MoonStars
        } else {
            Sun
        }
    }
    PARTLY_CLOUDY -> {
        val hour = now().toJakartaDateTime().hour
        if (hour in 18..24) {
            CloudMoon
        } else {
            CloudSun
        }
    }
    MOSTLY_CLOUDY -> Cloud
    OVERCAST -> Clouds
    HAZE -> Clouds // TODO
    SMOKE -> Clouds // TODO
    FOG -> Clouds // TODO
    LIGHT_RAIN -> LightRain
    RAIN -> Rain
    HEAVY_RAIN -> Rain
    ISOLATED_SHOWER -> Rain
    SEVERE_THUNDERSTORM -> Thunderstorm
}

fun WeatherCode.asColor(): Color = when (this) {
    CLEAR_SKIES,
    PARTLY_CLOUDY,
    MOSTLY_CLOUDY -> VividSkyBlue
    OVERCAST,
    HAZE,
    SMOKE,
    FOG -> Color.LightGray
    LIGHT_RAIN,
    RAIN,
    ISOLATED_SHOWER -> WeldonBlue
    HEAVY_RAIN,
    SEVERE_THUNDERSTORM -> StormCloud
}