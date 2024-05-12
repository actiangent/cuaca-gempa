package com.actiangent.cuacagempa.core.model

data class UserData(
    val userRegencyIds: Set<String>,
    val temperatureUnit: TemperatureUnit,
    val darkThemeConfig: DarkThemeConfig,
    val hasDoneOnboarding: Boolean,
) {

    companion object {
        fun default() = UserData(
            userRegencyIds = emptySet(),
            temperatureUnit = TemperatureUnit.CELSIUS,
            darkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
            hasDoneOnboarding = false,
        )

    }
}
