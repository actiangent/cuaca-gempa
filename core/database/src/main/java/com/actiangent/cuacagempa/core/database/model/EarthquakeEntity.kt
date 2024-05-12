package com.actiangent.cuacagempa.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(
    tableName = "earthquake",
    indices = [Index(value = ["datetime"])]
)
data class EarthquakeEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "datetime")
    val dateTime: Instant,

    val latitude: Double,

    val longitude: Double,

    val magnitude: Double,

    val depth: Int,

    @ColumnInfo(name = "shakemap_endpoint")
    val shakemapEndpoint: String? = null,
)