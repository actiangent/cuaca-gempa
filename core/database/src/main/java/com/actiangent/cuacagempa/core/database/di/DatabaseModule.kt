package com.actiangent.cuacagempa.core.database.di

import android.content.Context
import androidx.room.Room
import com.actiangent.cuacagempa.core.database.WeatherQuakeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesWeatherQuakeDatabase(
        @ApplicationContext context: Context,
    ): WeatherQuakeDatabase = Room.databaseBuilder(
        context,
        WeatherQuakeDatabase::class.java,
        "weather-quake-database",
    ).build()

}