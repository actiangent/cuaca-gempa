package com.actiangent.cuacagempa.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.actiangent.cuacagempa.core.database.dao.ProvinceDao
import com.actiangent.cuacagempa.core.database.dao.ProvinceFtsDao
import com.actiangent.cuacagempa.core.database.dao.RegencyDao
import com.actiangent.cuacagempa.core.database.dao.RegencyFtsDao
import com.actiangent.cuacagempa.core.database.dao.WeatherDao
import com.actiangent.cuacagempa.core.database.model.ProvinceEntity
import com.actiangent.cuacagempa.core.database.model.ProvinceL10nEnEntity
import com.actiangent.cuacagempa.core.database.model.ProvinceL10nEnFtsEntity
import com.actiangent.cuacagempa.core.database.model.ProvinceL10nIdEntity
import com.actiangent.cuacagempa.core.database.model.ProvinceL10nIdFtsEntity
import com.actiangent.cuacagempa.core.database.model.RegencyEntity
import com.actiangent.cuacagempa.core.database.model.RegencyL10nEnEntity
import com.actiangent.cuacagempa.core.database.model.RegencyL10nEnFtsEntity
import com.actiangent.cuacagempa.core.database.model.RegencyL10nIdEntity
import com.actiangent.cuacagempa.core.database.model.RegencyL10nIdFtsEntity
import com.actiangent.cuacagempa.core.database.model.RegencyWeatherEntity
import com.actiangent.cuacagempa.core.database.util.InstantTypeConverter
import com.actiangent.cuacagempa.core.database.util.LocalDateTimeTypeConverter

@Database(
    entities = [
        ProvinceEntity::class,
        ProvinceL10nIdEntity::class,
        ProvinceL10nEnEntity::class,
        ProvinceL10nEnFtsEntity::class,
        ProvinceL10nIdFtsEntity::class,
        RegencyEntity::class,
        RegencyL10nIdEntity::class,
        RegencyL10nEnEntity::class,
        RegencyL10nEnFtsEntity::class,
        RegencyL10nIdFtsEntity::class,
        RegencyWeatherEntity::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    InstantTypeConverter::class,
    LocalDateTimeTypeConverter::class,
)
abstract class WeatherQuakeDatabase : RoomDatabase() {

    abstract fun provinceDao(): ProvinceDao

    abstract fun regencyDao(): RegencyDao

    abstract fun weatherDao(): WeatherDao

    abstract fun provinceFtsDao(): ProvinceFtsDao

    abstract fun regencyFtsDao(): RegencyFtsDao
}