package com.actiangent.cuacagempa.core.data.di

import android.location.Geocoder
import com.actiangent.cuacagempa.core.data.repository.CompositeLocationWeatherRepository
import com.actiangent.cuacagempa.core.data.repository.LocationWeatherRepository
import com.actiangent.cuacagempa.core.data.repository.location.DefaultLocationRepository
import com.actiangent.cuacagempa.core.data.repository.location.LocationRepository
import com.actiangent.cuacagempa.core.data.repository.preferences.DefaultUserDataRepository
import com.actiangent.cuacagempa.core.data.repository.preferences.UserDataRepository
import com.actiangent.cuacagempa.core.data.repository.weather.DefaultWeatherRepository
import com.actiangent.cuacagempa.core.data.repository.weather.WeatherRepository
import com.actiangent.cuacagempa.core.database.dao.WeatherDao
import com.actiangent.cuacagempa.core.datastore.WeatherQuakePreferencesDataSource
import com.actiangent.cuacagempa.core.location.LocationProvider
import com.actiangent.cuacagempa.core.network.RemoteWeatherDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideUserDataRepository(
        preferencesDataSource: WeatherQuakePreferencesDataSource
    ): UserDataRepository = DefaultUserDataRepository(preferencesDataSource)

    @Singleton
    @Provides
    fun provideWeatherRepository(
        remoteWeatherDataSource: RemoteWeatherDataSource,
        weatherDao: WeatherDao
    ): WeatherRepository = DefaultWeatherRepository(remoteWeatherDataSource, weatherDao)

    @Singleton
    @Provides
    fun provideLocationRepository(
        locationProvider: LocationProvider,
        geocoder: Geocoder
    ): LocationRepository = DefaultLocationRepository(
        locationProvider, geocoder
    )

    @Singleton
    @Provides
    fun provideLocationWeatherRepository(
        weatherRepository: WeatherRepository,
        userDataRepository: UserDataRepository
    ): LocationWeatherRepository = CompositeLocationWeatherRepository(
        weatherRepository, userDataRepository
    )

}