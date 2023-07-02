package com.example.travels_map.utils.location

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import com.yandex.mapkit.location.FilteringMode
import com.yandex.mapkit.location.LocationListener
import com.yandex.mapkit.location.LocationManager

object LocationManagerUtil {

    private const val REQUEST_CODE_LOCATION_PERMISSIONS = 200

    private const val DESIRED_ACCURACY_M = 0.0
    private const val MIN_TIME_MS = 0L
    private const val MIN_DISTANCE_M = 0.0
    private const val ALLOW_USE_IN_BACKGROUND = true

    private val PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    fun Context.isLocationPermissionsGranted(): Boolean {
        val coarseLocationPermission = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        val fineLocationPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)

        return !(coarseLocationPermission != PackageManager.PERMISSION_GRANTED && fineLocationPermission != PackageManager.PERMISSION_GRANTED)
    }

    fun Activity.requestLocationPermissions() {
        requestPermissions(PERMISSIONS, REQUEST_CODE_LOCATION_PERMISSIONS)
    }

    fun LocationManager.subscribeForLocationUpdates(locationListener: LocationListener) {
        subscribeForLocationUpdates(
            DESIRED_ACCURACY_M,
            MIN_TIME_MS,
            MIN_DISTANCE_M,
            ALLOW_USE_IN_BACKGROUND,
            FilteringMode.ON,
            locationListener,
        )
    }
}