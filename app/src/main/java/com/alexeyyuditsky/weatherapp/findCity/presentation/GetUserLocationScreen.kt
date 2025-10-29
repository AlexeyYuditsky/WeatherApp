package com.alexeyyuditsky.weatherapp.findCity.presentation

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority

@Composable
fun GetUserLocationScreen(
    onSuccess: (Double, Double) -> Unit,
    onFailed: (String) -> Unit,
) {
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(LocalContext.current)

    val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
        .setWaitForAccurateLocation(false)
        .build()

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                fusedLocationClient.removeLocationUpdates(this)
                onSuccess.invoke(location.latitude, location.longitude)
            }
        }
    }

    val requestUpdates: () -> Unit = {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        ).addOnFailureListener { e ->
            onFailed.invoke(e.message ?: "failed to get location")
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            val isGranted = permissions.any { it.value }
            if (isGranted)
                getUserLocation(
                    fusedLocationClient = fusedLocationClient,
                    onLocationReceived = { latitude, longitude ->
                        onSuccess.invoke(latitude, longitude)
                    },
                    requestUpdates = requestUpdates
                )
            else
                onFailed.invoke("Permission denied")
        }
    )

    isGeoLocationEnabled(
        locationRequest = locationRequest,
        context = context,
        isGeoLocationEnabled = {
            val fineGranted = ActivityCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED
            val coarseGranted =
                ActivityCompat.checkSelfPermission(context, ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED

            if (fineGranted || coarseGranted)
                getUserLocation(
                    fusedLocationClient = fusedLocationClient,
                    onLocationReceived = { latitude, longitude ->
                        onSuccess.invoke(latitude, longitude)
                    },
                    requestUpdates = requestUpdates
                )
            else
                locationPermissionLauncher.launch(
                    input = arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
                )
        },
        isGeoLocationDisabled = onFailed
    )
}

private fun isGeoLocationEnabled(
    locationRequest: LocationRequest,
    context: Context,
    isGeoLocationEnabled: () -> Unit,
    isGeoLocationDisabled: (String) -> Unit,
) = LocationServices.getSettingsClient(context).checkLocationSettings(
    LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build()
).addOnCompleteListener { taskResult ->
    try {
        taskResult.getResult(ApiException::class.java)
        isGeoLocationEnabled.invoke()
    } catch (_: ApiException) {
        isGeoLocationDisabled.invoke("Location is disabled")
    }
}

@RequiresPermission(anyOf = [ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION])
private fun getUserLocation(
    fusedLocationClient: FusedLocationProviderClient,
    onLocationReceived: (Double, Double) -> Unit,
    requestUpdates: () -> Unit,
) = fusedLocationClient.lastLocation.addOnSuccessListener { location ->
    location?.let {
        onLocationReceived.invoke(location.latitude, location.longitude)
    } ?: requestUpdates.invoke()
}