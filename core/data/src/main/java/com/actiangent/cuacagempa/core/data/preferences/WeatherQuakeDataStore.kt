package com.actiangent.cuacagempa.core.data.preferences

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.actiangent.cuacagempa.core.data.model.UserLocationData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// TODO - change to proto datastore
private val Context.weatherQuakeDataStore: DataStore<Preferences> by preferencesDataStore(name = "userdata")

@Singleton
class WeatherQuakeDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {

    val userLocationData = context.weatherQuakeDataStore.data
        .map { preference ->
            UserLocationData(
                latitude = preference[PreferencesKeys.LATITUDE],
                longitude = preference[PreferencesKeys.LONGITUDE],
                province = preference[PreferencesKeys.PROVINCE_NAME],
            )
        }

    val districtId = context.weatherQuakeDataStore.data
        .map { preference ->
            preference[PreferencesKeys.DISTRICT_ID] ?: ""
        }

    suspend fun setLocationCoordinate(coordinate: Pair<Double, Double>) {
        Log.d("Coordinate update", "lat: ${coordinate.first}, lon: ${coordinate.second}")
        context.weatherQuakeDataStore.edit { preferences ->
            preferences[PreferencesKeys.LATITUDE] = coordinate.first
            preferences[PreferencesKeys.LONGITUDE] = coordinate.second
        }
    }

    suspend fun setProvinceName(provinceName: String?) {
        provinceName?.let {
            context.weatherQuakeDataStore.edit { preferences ->
                preferences[PreferencesKeys.PROVINCE_NAME] = provinceName
            }
        }
    }

    suspend fun setDistrictId(districtId: String) {
        context.weatherQuakeDataStore.edit { preferences ->
            preferences[PreferencesKeys.DISTRICT_ID] = districtId
        }
    }


    private object PreferencesKeys {
        val LATITUDE = doublePreferencesKey("latitude")
        val LONGITUDE = doublePreferencesKey("longitude")
        val PROVINCE_NAME = stringPreferencesKey("province_name")
        val DISTRICT_ID = stringPreferencesKey("district_id")
    }

}