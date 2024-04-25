package com.actiangent.cuacagempa.core.location

import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine

fun locationFromLatLon(latitude: Double, longitude: Double) =
    Location(LocationManager.GPS_PROVIDER).apply {
        this.latitude = latitude
        this.longitude = longitude
    }

fun Location.asString(format: Int = Location.FORMAT_DEGREES): String {
    val latitude = Location.convert(latitude, format)
    val longitude = Location.convert(longitude, format)
    return "Location is: $latitude, $longitude"
}

@Suppress("deprecation")
@OptIn(ExperimentalCoroutinesApi::class)
suspend fun Geocoder.getAddress(location: Location): Address? =
    suspendCancellableCoroutine { continuation ->
        // Tiramisu = API 33
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getFromLocation(
                location.latitude,
                location.longitude,
                1
            ) { continuation.resume(it.firstOrNull(), null) }
        } else {
            val addresses = getFromLocation(location.latitude, location.longitude, 1)
            continuation.resume(addresses?.firstOrNull(), null)
        }
    }
