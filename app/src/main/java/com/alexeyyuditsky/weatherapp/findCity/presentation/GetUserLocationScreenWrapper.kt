package com.alexeyyuditsky.weatherapp.findCity.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority

@Composable
fun GetUserLocationScreenWrapper(
    onSuccess: (Double, Double) -> Unit,
    onFailed: (String) -> Unit,
) {
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
        .setWaitForAccurateLocation(false)
        .build()

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            val isGranted = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ).any { permissions[it] == true }

            if (isGranted)
                getUserLocation(
                    fusedLocationClient = fusedLocationClient,
                    onLocationReceived = { latitude, longitude ->
                        onSuccess.invoke(latitude, longitude)
                    }
                )
            else
                onFailed.invoke("Permission denied")
        })

    isLocationEnabled(
        locationRequest = locationRequest,
        context = context,
        onResult = { isEnabled ->
            if (isEnabled) {
                val fineGranted = ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

                val coarseGranted = ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

                if (fineGranted || coarseGranted)
                    getUserLocation(
                        fusedLocationClient = fusedLocationClient,
                        onLocationReceived = { latitude, longitude ->
                            onSuccess.invoke(latitude, longitude)
                        }
                    )
                else
                    locationPermissionLauncher.launch(
                        input = arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                        )
                    )
            } else
                onFailed.invoke("Location is not enabled!")
        }
    )
}

private fun isLocationEnabled(
    locationRequest: LocationRequest,
    context: Context,
    onResult: (Boolean) -> Unit,
) {
    val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
    val client = LocationServices.getSettingsClient(context)
    val task = client.checkLocationSettings(builder.build())

    task.addOnCompleteListener { taskResult ->
        try {
            taskResult.getResult(ApiException::class.java)
            onResult.invoke(true)
        } catch (_: ApiException) {
            onResult.invoke(false)
        }
    }
}

@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
private fun getUserLocation(
    fusedLocationClient: FusedLocationProviderClient,
    onLocationReceived: (Double, Double) -> Unit,
) = fusedLocationClient.lastLocation.addOnSuccessListener { location ->
    onLocationReceived.invoke(location.latitude, location.longitude)
}