package com.actiangent.cuacagempa.core.database.dao

import android.database.sqlite.SQLiteConstraintException
import androidx.room.*
import com.actiangent.cuacagempa.core.database.model.*
import com.actiangent.cuacagempa.core.model.*
import kotlinx.coroutines.flow.Flow
import java.io.IOException

@Dao
interface WeatherDao {

    @Query(
        """
        SELECT
            strftime('%Y-%m-%dT%H:%M:%S', weather_table.timestamp, 'localtime') AS timestamp,
            weather_table.code AS code,
            (CASE 
                WHEN LOWER(:temperaturePreference) = 'celsius' THEN weather_table.celsius 
                ELSE weather_table.fahrenheit 
            END) AS temperature,
            weather_table.humidity AS humidity
        FROM
            (
            SELECT 
                record_id
            FROM
                district_table
            WHERE
                district_id IN (:districtIds)
            LIMIT 1
            ) AS district_table
        JOIN
            weather_table
        ON
            weather_table.district_record_id = district_table.record_id
        WHERE
            DATE(timestamp) >= DATE('now')
        """
    )
    fun getCurrentWeathers(
        districtIds: Set<String>,
        temperaturePreference: String,
    ): Flow<List<Weather>>

    @Transaction
    suspend fun insertDistrictWeathers(district: DistrictEntity, weathers: List<WeatherEntity>) {
        val districtRecordId = try {
            insertDistrict(district)
        } catch (_: SQLiteConstraintException) {
            // An item that has same districtId
            getDistrictRecordId(district.districtId)
        }
        if (districtRecordId < 0) throw IOException("Error inserting to database")
        else {
            val weatherEntities = weathers.map { it.copy(districtRecordId = districtRecordId) }
            insertWeathers(weatherEntities)
        }
    }

    @Query("SELECT record_id FROM district_table WHERE district_id = :districtId")
    suspend fun getDistrictRecordId(districtId: String): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertDistrict(district: DistrictEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeathers(weathers: List<WeatherEntity>)

}