package com.actiangent.cuacagempa.core.database.model

import androidx.room.*
import kotlinx.datetime.Instant

@Entity(
    tableName = "district_table",
    indices = [Index(value = ["district_id"], unique = true)],
)
data class DistrictEntity(
    @ColumnInfo(name = "district_id")
    val districtId: String,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "record_id")
    val recordId: Int = 0,
)

@Entity(
    tableName = "weather_table",
    indices = [Index(value = ["timestamp", "district_record_id"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = DistrictEntity::class,
            parentColumns = ["record_id"],
            childColumns = ["district_record_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class WeatherEntity(
    val timestamp: Instant,

    val code: Int,

    val celsius: Double,

    val fahrenheit: Double,

    val humidity: Int,

    @ColumnInfo(name = "district_record_id")
    val districtRecordId: Long = 0,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "record_id")
    val recordId: Int = 0,
)