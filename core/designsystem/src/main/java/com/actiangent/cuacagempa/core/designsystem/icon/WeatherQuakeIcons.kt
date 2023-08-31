package com.actiangent.cuacagempa.core.designsystem.icon

import androidx.annotation.DrawableRes
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.actiangent.cuacagempa.core.designsystem.R
import com.actiangent.cuacagempa.core.designsystem.icon.Icon.DrawableResourceIcon
import com.actiangent.cuacagempa.core.designsystem.icon.Icon.ImageVectorIcon

object WeatherQuakeIcons {

    val ArrowBack = ImageVectorIcon(Icons.Rounded.ArrowBack)
    val SettingsFilled = ImageVectorIcon(Icons.Filled.Settings)
    val Cloud = DrawableResourceIcon(R.drawable.ic_cloud_24)
    val ClearNight = DrawableResourceIcon(R.drawable.ic_clear_night_24)
    val Sunny = DrawableResourceIcon(R.drawable.ic_sunny_24)
    val Rain = DrawableResourceIcon(R.drawable.ic_rainy_24)
    val Thermostat = DrawableResourceIcon(R.drawable.ic_device_thermostat_24)
    val Thunderstorm = DrawableResourceIcon(R.drawable.ic_thunderstorm_24)
    val HumidityPercentage = DrawableResourceIcon(R.drawable.ic_humidity_percentage_24)
    val PinDrop = DrawableResourceIcon(R.drawable.ic_pin_drop_24)
    val PartlyCloudyNight = DrawableResourceIcon(R.drawable.ic_partly_cloudy_night_24)
    val PartlyCloudyDay = DrawableResourceIcon(R.drawable.ic_partly_cloudy_day_24)
    val Earthquake = DrawableResourceIcon(R.drawable.ic_earthquake_24)
    val Foggy = DrawableResourceIcon(R.drawable.ic_foggy_24)
    val Air = DrawableResourceIcon(R.drawable.ic_air_24)
}

// taken from android/nowinandroid
sealed class Icon {
    data class ImageVectorIcon(val imageVector: ImageVector) : Icon()
    data class DrawableResourceIcon(@DrawableRes val id: Int) : Icon()

    companion object {

        @Composable
        fun Icon(
            icon: Icon,
            contentDescription: String?,
            modifier: Modifier = Modifier,
            tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
        ) = when (icon) {
            is ImageVectorIcon -> Icon(
                imageVector = icon.imageVector,
                contentDescription = contentDescription,
                modifier = modifier,
                tint = tint,
            )

            is DrawableResourceIcon -> Icon(
                painter = painterResource(id = icon.id),
                contentDescription = contentDescription,
                modifier = modifier,
                tint = tint,
            )
        }
    }
}