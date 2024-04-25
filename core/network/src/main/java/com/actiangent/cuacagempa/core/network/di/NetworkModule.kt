package com.actiangent.cuacagempa.core.network.di

import com.actiangent.cuacagempa.core.network.RemoteWeatherDataSource
import com.actiangent.cuacagempa.core.network.converter.TimestampInstantTypeConverter
import com.actiangent.cuacagempa.core.network.retrofit.WeatherRetrofit
import com.actiangent.cuacagempa.core.network.retrofit.baseUrl
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
            addTypeConverter(Instant::class.java, TimestampInstantTypeConverter())
            exceptionOnUnreadXml(false)
        }.build()
    )

    @Singleton
    @Provides
    fun provideRetrofit(
        client: OkHttpClient,
        xmlConverterFactory: TikXmlConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(xmlConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideRemoteWeatherDataSource(
        retrofit: Retrofit
    ): RemoteWeatherDataSource = WeatherRetrofit(retrofit)

}