package com.actiangent.cuacagempa.core.designsystem.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/*
 * note: Color naming is approximation.
 */

// Neutral Color
internal val Jumbo10 =
    Color(0xFF1A1C1E) // Light On Background & On Surface, Dark Background & Surface
internal val Jumbo90 = Color(0xFFE2E2E5) // Dark On Background and On Surface
internal val Jumbo99 = Color(0xFFFCFCFF) // Light On Background and On Surface

// Primary Color
internal val Lochmara10 = Color(0xFFCEE5FF) // Light On Primary Container
internal val Lochmara20 = Color(0xFF003353) // Dark On Primary
internal val Lochmara30 = Color(0xFF004A75) // Dark Primary Container
internal val Lochmara40 = Color(0xFF00639A) // Light Primary
internal val Lochmara80 = Color(0xFF95CCFF) // Dark Primary
internal val Lochmara90 = Color(0xFF004A75) // Light Primary Container
internal val Lochmara100 = Color.White  // Light On Primary

// Neutral Variant Color
internal val PaleSky30 = Color(0xFF42474E) // Light On Surface Variant & Dark Surface Variant
internal val PaleSky50 = Color(0xFF72777F) // Light Outline
internal val PaleSky60 = Color(0xFF8C9198) // Dark Outline
internal val PaleSky80 = Color(0xFFC2C7CF) // Dark On Surface Variant
internal val PaleSky90 = Color(0xFFC2C7CF) // Light Surface Variant

// Secondary Color
internal val SlateGray10 = Color(0xFF0E1D2A) // Light On Secondary Container
internal val SlateGray20 = Color(0xFF233240) // Dark On Secondary
internal val SlateGray30 = Color(0xFF3A4857) // Dark Secondary Container
internal val SlateGray40 = Color(0xFF51606F) // Light Secondary
internal val SlateGray80 = Color(0xFFB9C8DA) // Dark Secondary
internal val SlateGray90 = Color(0xFFD5E4F6) // Light Secondary Container
internal val SlateGray100 = Color.White // Light On Secondary

/*
 * Brush, taken from https://webgradients.com/
 */

// Day - (Clear - Mostly Cloudy)
val WinterNeva = Brush.verticalGradient(
    colors = listOf(
        Color(0xFFaccbee),
        Color(0xFFe7f0fd)
    )
)

// Day - (Overcast, Haze, Smoke)
val PremiumWhite = Brush.verticalGradient(
    colors = listOf(
        Color(0xFFd5d4d0),
        Color(0xFFd5d4d0),
        Color(0xFFeeeeec),
        Color(0xFFefeeec),
        Color(0xFFe9e9e7)
    )
)

// Day - (Light Rain, Rain, Isolated Shower)
val FebruaryInk = Brush.verticalGradient(
    colors = listOf(
        Color(0xFFc3cfe2),
        Color(0xFFf5f7fa)
    )
)

// Day - (Heavy Rain, Severe Thunderstorm)
val MountainRock = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF868f96),
        Color(0xFF596164)
    )
)

// Night - (Clear - Mostly Cloudy)
val NightSky = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF1e3c72),
        Color(0xFF2a5298)
    )
)

// Night - (Overcast, Haze, Smoke)
val SolidStone = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF243949),
        Color(0xFF517fa4)
    )
)

// Night - (Light Rain, Rain, Isolated Shower)
val EternalConstance = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF09203f),
        Color(0xFF537895)
    )
)

// Night - (Heavy Rain, Severe Thunderstorm)
val ViciousStance = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF29323c),
        Color(0xFF485563)
    )
)