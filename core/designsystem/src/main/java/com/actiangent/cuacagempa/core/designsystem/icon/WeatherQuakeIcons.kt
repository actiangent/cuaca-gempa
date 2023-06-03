package com.actiangent.cuacagempa.core.designsystem.icon

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.ui.graphics.vector.ImageVector
import com.actiangent.cuacagempa.core.designsystem.R
import com.actiangent.cuacagempa.core.designsystem.icon.Icon.DrawableResourceIcon
import com.actiangent.cuacagempa.core.designsystem.icon.Icon.ImageVectorIcon

object WeatherQuakeIcons {

    val ArrowBack = ImageVectorIcon(Icons.Rounded.ArrowBack)
    val Cloud = DrawableResourceIcon(R.drawable.ic_cloud)
    val CloudMoon = DrawableResourceIcon(R.drawable.ic_cloud_moon)
    val CloudSun = DrawableResourceIcon(R.drawable.ic_cloud_sun)
    val Clouds = DrawableResourceIcon(R.drawable.ic_clouds)
    val Compass = DrawableResourceIcon(R.drawable.ic_compass)
    val LightRain = DrawableResourceIcon(R.drawable.ic_light_rain)
    val MoonStars = DrawableResourceIcon(R.drawable.ic_moon_stars)
    val Rain = DrawableResourceIcon(R.drawable.ic_rain)
    val Sun = DrawableResourceIcon(R.drawable.ic_sun)
    val Thermometer = DrawableResourceIcon(R.drawable.ic_thermometer)
    val Thunderstorm = DrawableResourceIcon(R.drawable.ic_thunderstorm)
    val WaterDrop = DrawableResourceIcon(R.drawable.ic_water_drop)

}

// taken from android/nowinandroid
sealed class Icon {
    data class ImageVectorIcon(val imageVector: ImageVector) : Icon()
    data class DrawableResourceIcon(@DrawableRes val id: Int) : Icon()
}