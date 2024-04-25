package com.actiangent.cuacagempa.core.designsystem.icon

import androidx.annotation.DrawableRes
import androidx.annotation.IntRange
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.actiangent.cuacagempa.core.designsystem.R
import com.actiangent.cuacagempa.core.designsystem.icon.Icon.DrawableResourceIcon
import com.actiangent.cuacagempa.core.designsystem.icon.Icon.ImageVectorIcon
import com.actiangent.cuacagempa.core.model.WeatherCondition

object WeatherQuakeIcons {

    val ArrowBack = ImageVectorIcon(Icons.Rounded.ArrowBack)
    val SettingsFilled = ImageVectorIcon(Icons.Filled.Settings)
    val Cloud = DrawableResourceIcon(R.drawable.ic_cloud_24)
    val ClearNight = DrawableResourceIcon(R.drawable.ic_clear_night_24)
    val ClearDay = DrawableResourceIcon(R.drawable.ic_clear_day_24)
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
    val ArrowDropDown = DrawableResourceIcon(R.drawable.ic_arrow_drop_down_24)
    val ArrowDropUp = DrawableResourceIcon(R.drawable.ic_arrow_drop_up_24)
    val WeatherMix = DrawableResourceIcon(R.drawable.ic_weather_mix_24)
    val ArrowForward = ImageVectorIcon(Icons.Rounded.ArrowForward)
    val Clear = ImageVectorIcon(Icons.Rounded.Clear)
    val Search = ImageVectorIcon(Icons.Rounded.Search)
    val AddLocation = DrawableResourceIcon(R.drawable.ic_add_location_24)
    val LocationAdded = DrawableResourceIcon(R.drawable.ic_location_added_24)
    val Add = ImageVectorIcon(Icons.Rounded.Add)
    val Check = ImageVectorIcon(Icons.Rounded.Check)
}

fun WeatherCondition.icon(
    @IntRange(from = 0, to = 24) hour: Int,
): Icon {
    return when (this) {
        WeatherCondition.CLEAR_SKIES ->
            if (hour in 5..18) WeatherQuakeIcons.ClearDay
            else WeatherQuakeIcons.ClearNight

        WeatherCondition.PARTLY_CLOUDY ->
            if (hour in 5..18) WeatherQuakeIcons.PartlyCloudyDay
            else WeatherQuakeIcons.PartlyCloudyNight

        WeatherCondition.MOSTLY_CLOUDY -> WeatherQuakeIcons.Cloud
        WeatherCondition.OVERCAST,
        WeatherCondition.HAZE,
        WeatherCondition.SMOKE,
        WeatherCondition.FOG -> WeatherQuakeIcons.Foggy

        WeatherCondition.LIGHT_RAIN,
        WeatherCondition.RAIN,
        WeatherCondition.HEAVY_RAIN,
        WeatherCondition.ISOLATED_SHOWER -> WeatherQuakeIcons.Rain

        WeatherCondition.SEVERE_THUNDERSTORM -> WeatherQuakeIcons.Thunderstorm
        WeatherCondition.UNKNOWN -> WeatherQuakeIcons.WeatherMix
    }
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