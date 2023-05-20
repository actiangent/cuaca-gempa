package com.actiangent.cuacagempa.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.actiangent.cuacagempa.core.database.dao.WeatherDao
import com.actiangent.cuacagempa.core.database.model.DistrictEntity
import com.actiangent.cuacagempa.core.database.model.WeatherEntity
import com.actiangent.cuacagempa.core.database.util.InstantTypeConverter
import com.actiangent.cuacagempa.core.database.util.LocalDateTimeTypeConverter

@Database(
    entities = [
        DistrictEntity::class,
        WeatherEntity::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    InstantTypeConverter::class,
    LocalDateTimeTypeConverter::class,
)
abstract class WeatherQuakeDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}