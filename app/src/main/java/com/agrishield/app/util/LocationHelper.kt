package com.agrishield.app.util

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.math.sqrt

class LocationHelper(private val context: Context) {

    private val fusedClient = LocationServices.getFusedLocationProviderClient(context)

    /**
     * Emits GPS location updates as a Flow.
     * First emits last known location immediately (no wait),
     * then emits fresh updates as they arrive.
     *
     * Requires ACCESS_FINE_LOCATION permission.
     */
    @SuppressLint("MissingPermission")
    fun getLocationFlow(): Flow<Location> = callbackFlow {
        // Emit last known immediately
        fusedClient.lastLocation.addOnSuccessListener { loc ->
            loc?.let { trySend(it) }
        }

        val request = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            10_000L    // every 10 seconds
        )
            .setMinUpdateDistanceMeters(50f)   // only update if moved 50m
            .build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { trySend(it) }
            }
        }

        fusedClient.requestLocationUpdates(request, callback, Looper.getMainLooper())
        awaitClose { fusedClient.removeLocationUpdates(callback) }
    }
}

/**
 * Returns the Maharashtra district name for a given lat/lon.
 * Uses a bounding-box lookup against known district centroids.
 * Returns empty string if no match found within 0.6 degrees.
 */
fun districtForCoordinates(lat: Double, lon: Double): String {
    data class DistrictCentroid(val name: String, val lat: Double, val lon: Double)

    val centroids = listOf(
        DistrictCentroid("Yavatmal",20.3888,78.1204),
        DistrictCentroid("Amravati",20.9320,77.7523),
        DistrictCentroid("Akola",20.7090,77.0082),
        DistrictCentroid("Buldhana",20.5292,76.1842),
        DistrictCentroid("Washim",20.1100,77.1300),
        DistrictCentroid("Nagpur",21.1458,79.0882),
        DistrictCentroid("Wardha",20.7453,78.6022),
        DistrictCentroid("Chandrapur",19.9615,79.2961),
        DistrictCentroid("Bhandara",21.1666,79.6500),
        DistrictCentroid("Gondia",21.4608,80.1917),
        DistrictCentroid("Aurangabad",19.8762,75.3433),
        DistrictCentroid("Jalna",19.8347,75.8816),
        DistrictCentroid("Beed",18.9891,75.7601),
        DistrictCentroid("Latur",18.4088,76.5604),
        DistrictCentroid("Osmanabad",18.1860,76.0359),
        DistrictCentroid("Nanded",19.1383,77.3210),
        DistrictCentroid("Hingoli",19.7160,77.1490),
        DistrictCentroid("Parbhani",19.2704,76.7747),
        DistrictCentroid("Nashik",20.0059,73.7898),
        DistrictCentroid("Ahmednagar",19.0952,74.7389),
        DistrictCentroid("Dhule",20.9020,74.7748),
        DistrictCentroid("Nandurbar",21.3700,74.2400),
        DistrictCentroid("Jalgaon",21.0077,75.5626),
        DistrictCentroid("Pune",18.5204,73.8567),
        DistrictCentroid("Satara",17.6805,74.0183),
        DistrictCentroid("Solapur",17.6599,75.9064),
        DistrictCentroid("Kolhapur",16.7050,74.2433),
        DistrictCentroid("Sangli",16.8524,74.5815),
        DistrictCentroid("Raigad",18.5158,73.1298),
        DistrictCentroid("Ratnagiri",16.9902,73.3120),
        DistrictCentroid("Sindhudurg",16.3500,73.8200),
        DistrictCentroid("Thane",19.2183,72.9781),
        DistrictCentroid("Palghar",19.6967,72.7697),
        DistrictCentroid("Gadchiroli",20.1809,79.9444),
        DistrictCentroid("Mumbai Suburban",19.0760,72.8777)
    )

    // Find nearest centroid within 0.6 degree radius (~65 km)
    val threshold = 0.6
    return centroids.minByOrNull { c ->
        val dLat = c.lat - lat
        val dLon = c.lon - lon
        dLat * dLat + dLon * dLon
    }?.let { nearest ->
        val dist = sqrt(
            (nearest.lat - lat) * (nearest.lat - lat) +
            (nearest.lon - lon) * (nearest.lon - lon)
        )
        if (dist <= threshold) nearest.name else ""
    } ?: ""
}

fun generateReportId(): String {
    val ts = java.time.LocalDate.now().toString().replace("-", "")
    val rand = (1000..9999).random()
    return "REP-$ts-$rand"
}
