package com.actiangent.cuacagempa.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.actiangent.cuacagempa.core.database.model.RegencyWeatherEntity
import com.actiangent.cuacagempa.core.model.Weather
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Query(
        """
        SELECT
            strftime('%Y-%m-%dT%H:%M:%S', datetime, 'localtime') AS dateTime,
            weather_code as code,
            (CASE 
                WHEN LOWER(:temperatureUnit) = 'celsius' THEN regency_weather.temperature_celsius 
                ELSE regency_weather.temperature_fahrenheit 
            END) AS temperature,
            humidity,
            wind_direction_cardinal as cardinal,
            wind_speed_knot as knot
        FROM
            regency_weather
        WHERE
            regency_id = :regencyId AND DATE(dateTime) >= DATE('now') 
        ORDER BY dateTime
        """
    )
    // DATE('now') returns the current UTC date.
    fun getRegencyWeatherForecast(
        regencyId: String,
        temperatureUnit: String
    ): Flow<List<Weather>>

    @Insert
    suspend fun insertRegencyWeathers(weathers: List<RegencyWeatherEntity>)

    @Query("DELETE FROM regency_weather WHERE DATE(datetime) < DATE('now')")
    suspend fun deleteOldRegencyWeathers()
}