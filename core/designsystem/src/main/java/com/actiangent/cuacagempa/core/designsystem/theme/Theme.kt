package com.actiangent.cuacagempa.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Lochmara80,
    onPrimary = Lochmara20,
    primaryContainer = Lochmara30,
    onPrimaryContainer = Lochmara90,
    secondary = SlateGray80,
    onSecondary = SlateGray20,
    secondaryContainer = SlateGray30,
    onSecondaryContainer = SlateGray90,
    background = Jumbo10,
    onBackground = Jumbo90,
    surface = Jumbo10,
    onSurface = Jumbo90,
    outline = PaleSky60,
    surfaceVariant = PaleSky30,
    onSurfaceVariant = PaleSky80

    /* Other colors to override
    error = ,
    ...
    */
)

private val LightColorScheme = lightColorScheme(
    primary = Lochmara40,
    onPrimary = Lochmara100,
    primaryContainer = Lochmara90,
    onPrimaryContainer = Lochmara10,
    secondary = SlateGray40,
    onSecondary = SlateGray100,
    secondaryContainer = SlateGray90,
    onSecondaryContainer = SlateGray10,
    background = Jumbo99,
    onBackground = Jumbo10,
    surface = Jumbo99,
    onSurface = Jumbo10,
    outline = PaleSky50,
    surfaceVariant = PaleSky90,
    onSurfaceVariant = PaleSky30

    /* Other colors to override
    error = ,
    ...
    */
)

@Composable
fun WeatherQuakeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    val typography = Typography(
        displayLarge = MaterialTheme.typography.displayLarge.copy(fontFamily = poppins),
        displayMedium = MaterialTheme.typography.displayMedium.copy(fontFamily = poppins),
        displaySmall = MaterialTheme.typography.displaySmall.copy(fontFamily = poppins),
        headlineLarge = MaterialTheme.typography.headlineLarge.copy(fontFamily = poppins),
        headlineMedium = MaterialTheme.typography.headlineMedium.copy(fontFamily = poppins),
        headlineSmall = MaterialTheme.typography.headlineSmall.copy(fontFamily = poppins),
        titleLarge = MaterialTheme.typography.titleLarge.copy(fontFamily = poppins),
        titleMedium = MaterialTheme.typography.titleMedium.copy(fontFamily = poppins),
        titleSmall = MaterialTheme.typography.titleSmall.copy(fontFamily = poppins),
        bodyLarge = MaterialTheme.typography.bodyLarge.copy(fontFamily = poppins),
        bodyMedium = MaterialTheme.typography.bodyMedium.copy(fontFamily = poppins),
        bodySmall = MaterialTheme.typography.bodySmall.copy(fontFamily = poppins),
        labelLarge = MaterialTheme.typography.labelLarge.copy(fontFamily = poppins),
        labelMedium = MaterialTheme.typography.labelMedium.copy(fontFamily = poppins),
        labelSmall = MaterialTheme.typography.labelSmall.copy(fontFamily = poppins),
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}
