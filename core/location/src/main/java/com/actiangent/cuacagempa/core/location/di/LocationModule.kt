package com.actiangent.cuacagempa.core.location.di

import android.content.Context
import android.location.Geocoder
import com.actiangent.cuacagempa.core.location.LocationProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {

    @Singleton
    @Provides
    fun provideLocationProvider(@ApplicationContext context: Context): LocationProvider =
        LocationProvider(context)

    @Singleton
    @Provides
    fun provideGeocoder(@ApplicationContext context: Context): Geocoder =
        Geocoder(context, Locale("in-ID"))

}