package com.actiangent.cuacagempa.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.actiangent.cuacagempa.core.database.model.EarthquakeEntity
import com.actiangent.cuacagempa.core.model.Earthquake
import kotlinx.coroutines.flow.Flow

@Dao
interface EarthquakeDao {

    @Query(
        """
            SELECT
                strftime('%Y-%m-%dT%H:%M:%S', datetime, 'localtime') AS dateTime,
                latitude as latitude,
                longitude as longitude,
                magnitude as magnitude,
                depth as depth,
                shakemap_endpoint as shakemapEndpoint
            FROM
                earthquake
            ORDER BY
                DATETIME(datetime) DESC
            LIMIT 
                1
        """
    )
    fun getRecentEarthQuake(): Flow<Earthquake>

    @Query(
        """
            SELECT
                strftime('%Y-%m-%dT%H:%M:%S', datetime, 'localtime') AS dateTime,
                latitude as latitude,
                longitude as longitude,
                magnitude as magnitude,
                depth as depth,
                shakemap_endpoint as shakemapEndpoint
            FROM
                earthquake
            WHERE
                magnitude >= 5.0
            LIMIT
                25
        """
    )
    fun getLatestCautiousEarthquakes(): Flow<List<Earthquake>>

    @Insert
    suspend fun insertEarthquakes(earthquakes: List<EarthquakeEntity>)

    @Query(
        """
        DELETE 
        FROM
            earthquake
        WHERE
            (DATE(datetime) < DATE('now', '-7 days') AND magnitude < 5.0) 
            OR
            (datetime NOT IN (SELECT datetime FROM earthquake WHERE magnitude >= 5.0 LIMIT 25) AND magnitude >= 5.0)
        """
    )
    suspend fun deleteOldEarthquakes()
}