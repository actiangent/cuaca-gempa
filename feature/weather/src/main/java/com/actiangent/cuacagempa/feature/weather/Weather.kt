package com.actiangent.cuacagempa.feature.weather

import android.util.LayoutDirection
import androidx.annotation.IntRange
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.util.lerp
import com.actiangent.cuacagempa.core.designsystem.theme.EternalConstance
import com.actiangent.cuacagempa.core.designsystem.theme.FebruaryInk
import com.actiangent.cuacagempa.core.designsystem.theme.MountainRock
import com.actiangent.cuacagempa.core.designsystem.theme.NightSky
import com.actiangent.cuacagempa.core.designsystem.theme.PremiumWhite
import com.actiangent.cuacagempa.core.designsystem.theme.SolidStone
import com.actiangent.cuacagempa.core.designsystem.theme.ViciousStance
import com.actiangent.cuacagempa.core.designsystem.theme.WinterNeva
import com.actiangent.cuacagempa.core.model.RegencyForecasts
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
import kotlinx.datetime.LocalDate
import kotlin.math.absoluteValue

sealed interface WeatherUiState {
    object Loading : WeatherUiState
    data class Success(
        val data: RegencyForecasts,
    ) : WeatherUiState

    data class Error(val message: String) : WeatherUiState
}

fun LocalDate.display(): String = "${dayOfMonth}/$monthNumber/$year"

internal fun WeatherCondition.brush(
    @IntRange(from = 0, to = 24) hour: Int,
) = if (hour in 5..18) {
    when (this) {
        CLEAR_SKIES, PARTLY_CLOUDY, MOSTLY_CLOUDY -> WinterNeva
        OVERCAST, HAZE, SMOKE, FOG -> PremiumWhite
        LIGHT_RAIN, RAIN, ISOLATED_SHOWER -> FebruaryInk
        HEAVY_RAIN, SEVERE_THUNDERSTORM -> MountainRock
        UNKNOWN -> Brush.verticalGradient(colors = listOf(Color.Gray))
    }
} else {
    when (this) {
        CLEAR_SKIES, PARTLY_CLOUDY, MOSTLY_CLOUDY -> NightSky
        OVERCAST, HAZE, SMOKE, FOG -> SolidStone
        LIGHT_RAIN, RAIN, ISOLATED_SHOWER -> EternalConstance
        HEAVY_RAIN, SEVERE_THUNDERSTORM -> ViciousStance
        UNKNOWN -> Brush.verticalGradient(colors = listOf(Color.Gray))
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun WeatherHorizontalPager(
    pageCount: Int,
    modifier: Modifier = Modifier,
    state: PagerState = rememberPagerState(),
    pageContent: @Composable () -> Unit,
) {
    HorizontalPager(
        pageCount = pageCount,
        state = state,
        modifier = modifier
    ) { page ->
        val isRtl =
            LocalContext.current.resources.configuration.layoutDirection == LayoutDirection.RTL
        Box(
            modifier = modifier
                .graphicsLayer {
                    val pageOffset =
                        ((state.currentPage - page) + state.currentPageOffsetFraction)
                    alpha = lerp(
                        start = 0f,
                        stop = 1f,
                        fraction = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f),
                    )

                    val fraction =
                        if (state.currentPage != page) -pageOffset else 1f - pageOffset
                    translationX = lerp(
                        start = size.width,
                        stop = 0f,
                        fraction = if (isRtl) 2f - fraction else fraction,
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            pageContent()
        }
    }
}