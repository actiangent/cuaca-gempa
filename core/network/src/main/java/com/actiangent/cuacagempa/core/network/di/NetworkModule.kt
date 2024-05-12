package com.actiangent.cuacagempa.core.network.di

import com.actiangent.cuacagempa.core.network.RemoteEarthquakeDataSource
import com.actiangent.cuacagempa.core.network.RemoteWeatherDataSource
import com.actiangent.cuacagempa.core.network.converter.DateTimeInstantTixXmlTypeConverter
import com.actiangent.cuacagempa.core.network.converter.DatetimeInstantGsonTypeAdapter
import com.actiangent.cuacagempa.core.network.converter.QualifiedTypeConverterFactory
import com.actiangent.cuacagempa.core.network.retrofit.EarthquakeRetrofit
import com.actiangent.cuacagempa.core.network.retrofit.WeatherRetrofit
import com.actiangent.cuacagempa.core.network.retrofit.baseUrl
import com.google.gson.GsonBuilder
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.datetime.Instant
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }).build()
    }

    @Singleton
    @Provides
    fun provideTikXmlConverterFactory(): TikXmlConverterFactory = TikXmlConverterFactory.create(
        TikXml.Builder().apply {
            addTypeConverter(Instant::class.java, DateTimeInstantTixXmlTypeConverter())
            exceptionOnUnreadXml(false)
        }.build()
    )

    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory =
        GsonConverterFactory.create(
            GsonBuilder()
                .registerTypeAdapter(Instant::class.java, DatetimeInstantGsonTypeAdapter())
                .create()
        )

    @Singleton
    @Provides
    fun provideQualifiedTypeConverterFactory(
        jsonConverterFactory: GsonConverterFactory,
        xmlConverterFactory: TikXmlConverterFactory,
    ): QualifiedTypeConverterFactory = QualifiedTypeConverterFactory(jsonConverterFactory, xmlConverterFactory)

    @Singleton
    @Provides
    fun provideRetrofit(
        client: OkHttpClient,
        converterFactory: QualifiedTypeConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideRemoteWeatherDataSource(
        retrofit: Retrofit
    ): RemoteWeatherDataSource = WeatherRetrofit(retrofit)

    @Singleton
    @Provides
    fun provideRemoteEarthquakeDataSource(
        retrofit: Retrofit
    ): RemoteEarthquakeDataSource = EarthquakeRetrofit(retrofit)


}