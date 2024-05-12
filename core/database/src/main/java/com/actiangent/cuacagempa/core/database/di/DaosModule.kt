package com.actiangent.cuacagempa.core.database.di

import com.actiangent.cuacagempa.core.database.WeatherQuakeDatabase
import com.actiangent.cuacagempa.core.database.dao.EarthquakeDao
import com.actiangent.cuacagempa.core.database.dao.ProvinceDao
import com.actiangent.cuacagempa.core.database.dao.ProvinceFtsDao
import com.actiangent.cuacagempa.core.database.dao.RegencyDao
import com.actiangent.cuacagempa.core.database.dao.RegencyFtsDao
import com.actiangent.cuacagempa.core.database.dao.WeatherDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {

    @Provides
    fun providesProvinceDao(
        database: WeatherQuakeDatabase,
    ): ProvinceDao = database.provinceDao()

    @Provides
    fun providesRegencyDao(
        database: WeatherQuakeDatabase,
    ): RegencyDao = database.regencyDao()

    @Provides
    fun providesProvinceFtsDao(
        database: WeatherQuakeDatabase,
    ): ProvinceFtsDao = database.provinceFtsDao()

    @Provides
    fun providesRegencyFtsDao(
        database: WeatherQuakeDatabase,
    ): RegencyFtsDao = database.regencyFtsDao()

    @Provides
    fun providesWeatherDao(
        database: WeatherQuakeDatabase,
    ): WeatherDao = database.weatherDao()

    @Provides
    fun providesEarthquakeDao(
        database: WeatherQuakeDatabase,
    ): EarthquakeDao = database.earthquakeDao()
}