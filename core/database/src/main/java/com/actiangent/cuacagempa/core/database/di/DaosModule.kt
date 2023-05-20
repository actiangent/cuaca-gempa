package com.actiangent.cuacagempa.core.database.di

import com.actiangent.cuacagempa.core.database.WeatherQuakeDatabase
import com.actiangent.cuacagempa.core.database.dao.WeatherDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {

    @Provides
    fun providesWeatherDao(
        database: WeatherQuakeDatabase,
    ): WeatherDao = database.weatherDao()

}