package com.actiangent.cuacagempa.core.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.actiangent.cuacagempa.core.database.WeatherQuakeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesWeatherQuakeDatabase(
        @ApplicationContext context: Context,
    ): WeatherQuakeDatabase = Room.databaseBuilder(
        context,
        WeatherQuakeDatabase::class.java,
        "weather-quake-database",
    )
        .createFromAsset("database/weatherquake.db")
        .addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                db.execSQL(
                    """
                    CREATE TRIGGER IF NOT EXISTS avoid_duplicate_timestamp_before_insert_regency_weather 
                       BEFORE INSERT ON regency_weather 
                       WHEN EXISTS(SELECT 1 FROM regency_weather WHERE timestamp=NEW.timestamp AND regency_id=NEW.regency_id)
                    BEGIN
                        DELETE FROM regency_weather WHERE timestamp=NEW.timestamp AND regency_id=NEW.regency_id;
                    END
                    """.trimIndent()
                )
            }
        })
        .build()

}