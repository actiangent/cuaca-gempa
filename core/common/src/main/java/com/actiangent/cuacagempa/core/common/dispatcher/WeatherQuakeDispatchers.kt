package com.actiangent.cuacagempa.core.common.dispatcher

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val weatherQuakeDispatcher: WeatherQuakeDispatchers)

enum class WeatherQuakeDispatchers {
    Default,
    IO,
}