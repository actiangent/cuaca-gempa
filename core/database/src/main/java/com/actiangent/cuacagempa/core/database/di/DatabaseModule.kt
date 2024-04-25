package com.actiangent.cuacagempa.core.database.di

import android.content.Context
import androidx.room.Room
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
        /*
        .addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                db.execSQL("INSERT INTO province_table (name, identifier, endpoint, record_id) VALUES ('Jakarta', 'jakarta', 'DKIJakarta', 7);")
                db.execSQL("INSERT INTO district_table (district_id, type, identifier, province_record_id) VALUES ('501195', 'land', 'central_jakarta', 7);")
            }
        })
         */
        .createFromAsset("database/weatherquake.db")
        .build()

}