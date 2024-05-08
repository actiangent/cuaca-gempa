package com.actiangent.cuacagempa.feature.weather

import androidx.annotation.IntRange
import androidx.compose.ui.graphics.Color
import com.actiangent.cuacagempa.core.model.WeatherCondition
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

internal fun WeatherCondition.gradient(
    @IntRange(from = 0, to = 24) hour: Int,
) = if (hour in 5..18) {
    when (this) {
        CLEAR_SKIES, PARTLY_CLOUDY, MOSTLY_CLOUDY -> WinterNeva
        OVERCAST, HAZE, SMOKE, FOG -> PremiumWhite
        LIGHT_RAIN, RAIN, ISOLATED_SHOWER -> FebruaryInk
        HEAVY_RAIN, SEVERE_THUNDERSTORM -> MountainRock
        UNKNOWN -> TwoColorGradient(start = Color.Gray, end = Color.DarkGray)
    }
} else {
    when (this) {
        CLEAR_SKIES, PARTLY_CLOUDY, MOSTLY_CLOUDY -> NightSky
        OVERCAST, HAZE, SMOKE, FOG -> SolidStone
        LIGHT_RAIN, RAIN, ISOLATED_SHOWER -> EternalConstance
        HEAVY_RAIN, SEVERE_THUNDERSTORM -> ViciousStance
        UNKNOWN -> TwoColorGradient(start = Color.Gray, end = Color.DarkGray)
    }
}

// Day - (Clear - Mostly Cloudy)
val WinterNeva = TwoColorGradient(
    start = Color(0xFFaccbee),
    end = Color(0xFFe7f0fd),
)

// Day - (Overcast, Haze, Smoke)
val PremiumWhite = TwoColorGradient(
    start = Color(0xFFd5d4d0),
    end = Color(0xFFe9e9e7),
)

// Day - (Light Rain, Rain, Isolated Shower)
val FebruaryInk = TwoColorGradient(
    start = Color(0xFFc3cfe2),
    end = Color(0xFFf5f7fa),
)

// Day - (Heavy Rain, Severe Thunderstorm)
val MountainRock = TwoColorGradient(
    start = Color(0xFF868f96),
    end = Color(0xFF596164),
)

// Night - (Clear - Mostly Cloudy)
val NightSky = TwoColorGradient(
    start = Color(0xFF1e3c72),
    end = Color(0xFF2a5298),
)

// Night - (Overcast, Haze, Smoke)
val SolidStone = TwoColorGradient(
    start = Color(0xFF243949),
    end = Color(0xFF517fa4),
)

// Night - (Light Rain, Rain, Isolated Shower)
val EternalConstance = TwoColorGradient(
    start = Color(0xFF09203f),
    end = Color(0xFF537895),
)

// Night - (Heavy Rain, Severe Thunderstorm)
val ViciousStance = TwoColorGradient(
    start = Color(0xFF29323c),
    end = Color(0xFF485563),
)

data class TwoColorGradient(
    val start: Color,
    val end: Color
)