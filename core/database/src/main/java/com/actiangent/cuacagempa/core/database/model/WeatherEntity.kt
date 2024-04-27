package com.actiangent.cuacagempa.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(
    tableName = "regency_weather",
    foreignKeys = [
        ForeignKey(
            entity = RegencyEntity::class,
            parentColumns = ["id"],
            childColumns = ["regency_id"],
        )
    ],
    indices = [Index(value = ["timestamp"])]
)
data class RegencyWeatherEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val timestamp: Instant,

    @ColumnInfo(name = "weather_code")
    val weatherCode: Int,

    @ColumnInfo(name = "temperature_celsius")
    val temperatureCelsius: Double,

    @ColumnInfo(name = "temperature_fahrenheit")
    val temperatureFahrenheit: Double,

    val humidity: Int,

    @ColumnInfo(name = "wind_direction_cardinal")
    val windDirectionCardinal: String,

    @ColumnInfo(name = "wind_direction_degree")
    val windDirectionDegree: Double,

    @ColumnInfo(name = "wind_speed_knot")
    val windSpeedKnot: Int,

    @ColumnInfo(name = "wind_speed_mph")
    val windSpeedMph: Double,

    @ColumnInfo(name = "wind_speed_kph")
    val windSpeedKph: Double,

    @ColumnInfo(name = "wind_speed_mps")
    val windSpeedMps: Double,

    @ColumnInfo(name = "regency_id")
    val regencyId: String = "0",
)