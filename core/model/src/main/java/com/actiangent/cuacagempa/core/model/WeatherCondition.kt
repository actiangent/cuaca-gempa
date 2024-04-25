package com.actiangent.cuacagempa.core.model

import com.actiangent.cuacagempa.core.model.WeatherCondition.CLEAR_SKIES
import com.actiangent.cuacagempa.core.model.WeatherCondition.FOG
import com.actiangent.cuacagempa.core.model.WeatherCondition.HAZE
import com.actiangent.cuacagempa.core.model.WeatherCondition.HEAVY_RAIN
import com.actiangent.cuacagempa.core.model.WeatherCondition.ISOLATED_SHOWER
import com.actiangent.cuacagempa.core.model.WeatherCondition.LIGHT_RAIN
import com.actiangent.cuacagempa.core.model.WeatherCondition.MOSTLY_CLOUDY
import com.actiangent.cuacagempa.core.model.WeatherCondition.OVERCAST
import com.actiangent.cuacagempa.core.model.WeatherCondition.PARTLY_CLOUDY
import com.actiangent.cuacagempa.core.model.WeatherCondition.RAIN
import com.actiangent.cuacagempa.core.model.WeatherCondition.SEVERE_THUNDERSTORM
import com.actiangent.cuacagempa.core.model.WeatherCondition.SMOKE
import com.actiangent.cuacagempa.core.model.WeatherCondition.UNKNOWN

enum class WeatherCondition {
    CLEAR_SKIES,
    PARTLY_CLOUDY,
    MOSTLY_CLOUDY,
    OVERCAST,
    HAZE,
    SMOKE,
    FOG,
    LIGHT_RAIN,
    RAIN,
    HEAVY_RAIN,
    ISOLATED_SHOWER,
    SEVERE_THUNDERSTORM,
    UNKNOWN;

    override fun toString(): String {
        // capitalize each word
        return name
            .split("_")
            .joinToString(" ") { word ->
                val range = 1 until word.length
                word.replaceRange(
                    range = range,
                    replacement = word.substring(range).lowercase()
                )
            }
    }
}

fun Int.asWeatherCondition(): WeatherCondition = when (this) {
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
    else -> UNKNOWN
}

