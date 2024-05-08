package com.actiangent.cuacagempa.core.data.di

import com.actiangent.cuacagempa.core.common.dispatcher.Dispatcher
import com.actiangent.cuacagempa.core.common.dispatcher.WeatherQuakeDispatchers.IO
import com.actiangent.cuacagempa.core.data.repository.CompositeUserDistrictWeatherRepository
import com.actiangent.cuacagempa.core.data.repository.DefaultRegencyRepository
import com.actiangent.cuacagempa.core.data.repository.RegencyRepository
import com.actiangent.cuacagempa.core.data.repository.UserDistrictWeatherRepository
import com.actiangent.cuacagempa.core.data.repository.location.DefaultLocationRepository
import com.actiangent.cuacagempa.core.data.repository.location.LocationRepository
import com.actiangent.cuacagempa.core.data.repository.preferences.DefaultUserDataRepository
import com.actiangent.cuacagempa.core.data.repository.preferences.UserDataRepository
import com.actiangent.cuacagempa.core.data.repository.weather.DefaultWeatherRepository
import com.actiangent.cuacagempa.core.data.repository.weather.WeatherRepository
import com.actiangent.cuacagempa.core.database.dao.ProvinceDao
import com.actiangent.cuacagempa.core.database.dao.ProvinceFtsDao
import com.actiangent.cuacagempa.core.database.dao.RegencyDao
import com.actiangent.cuacagempa.core.database.dao.RegencyFtsDao
import com.actiangent.cuacagempa.core.database.dao.WeatherDao
import com.actiangent.cuacagempa.core.datastore.WeatherQuakePreferencesDataSource
import com.actiangent.cuacagempa.core.location.LocationDataSource
import com.actiangent.cuacagempa.core.location.LocationProvider
import com.actiangent.cuacagempa.core.network.RemoteWeatherDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoriesModule {

    @Singleton
    @Provides
    fun provideLocationRepository(
        locationProvider: LocationProvider,
        @Dispatcher(IO) ioDispatcher: CoroutineDispatcher
    ): LocationRepository = DefaultLocationRepository(locationProvider, ioDispatcher)

    @Singleton
    @Provides
    fun provideUserDataRepository(
        preferencesDataSource: WeatherQuakePreferencesDataSource
    ): UserDataRepository = DefaultUserDataRepository(preferencesDataSource)

    @Singleton
    @Provides
    fun provideDistrictWeatherRepository(
        locationDataSource: LocationDataSource,
        provinceDao: ProvinceDao,
        regencyDao: RegencyDao,
        remoteWeatherDataSource: RemoteWeatherDataSource,
        regencyWeatherDao: WeatherDao,
    ): WeatherRepository =
        DefaultWeatherRepository(
            locationDataSource,
            provinceDao,
            regencyDao,
            remoteWeatherDataSource,
            regencyWeatherDao
        )

    @Singleton
    @Provides
    fun provideUserDistrictWeatherRepository(
        weatherRepository: WeatherRepository,
        userDataRepository: UserDataRepository,
    ): UserDistrictWeatherRepository =
        CompositeUserDistrictWeatherRepository(weatherRepository, userDataRepository)

    @Singleton
    @Provides
    fun provideSearchRegencyRepository(
        provinceDao: ProvinceDao,
        regencyDao: RegencyDao,
        provinceFtsDao: ProvinceFtsDao,
        regencyFtsDao: RegencyFtsDao,
    ): RegencyRepository =
        DefaultRegencyRepository(provinceDao, regencyDao, provinceFtsDao, regencyFtsDao)

}