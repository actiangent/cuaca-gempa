package com.actiangent.cuacagempa.core.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import com.google.android.gms.location.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine

@Suppress("deprecation")
@SuppressLint("MissingPermission") // still should request location permissions
@OptIn(ExperimentalCoroutinesApi::class)
class LocationProvider(context: Context) {

    private val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    // one-shot request current location
    suspend fun awaitCurrentLocation(): Location = suspendCancellableCoroutine { continuation ->

        // implement android.location.LocationListener#onLocationChanged
        val callback = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                continuation.resume(location, null)
                locationManager.removeUpdates(this)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0L,
                0f,
                callback
            )
        } else {
            // deprecated in api 30
            locationManager.requestSingleUpdate(
                LocationManager.GPS_PROVIDER,
                callback,
                Looper.getMainLooper()
            )
        }
    }

    fun isGpsEnabled() = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

}
