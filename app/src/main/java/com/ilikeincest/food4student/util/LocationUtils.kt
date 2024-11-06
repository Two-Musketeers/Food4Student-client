package com.ilikeincest.food4student.util
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.here.sdk.core.GeoCoordinates
import com.ilikeincest.food4student.MainActivity
import com.ilikeincest.food4student.viewmodel.MapViewModel

class LocationUtils(val context: Context) {

    private val _fusedLocationClient: FusedLocationProviderClient
            = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    fun requestLocationUpdates(viewModel: MapViewModel){
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                p0.lastLocation?.let {
                    val currentLocation = GeoCoordinates(it.latitude, it.longitude)
                    viewModel.updateCurrentLocation(currentLocation)
                }
            }
        }
        val locationRequest = LocationRequest
            .Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
            .build()

        _fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    fun hasLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestLocationPermissions(
        requestPermissionLauncher: ActivityResultLauncher<Array<String>>
    ) {
        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    fun handlePermissionResult(
        permissions: Map<String, Boolean>,
        mapViewModel: MapViewModel
    ) {
        if (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            && permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
            requestLocationUpdates(mapViewModel)
        } else {
            val rationaleRequired = ActivityCompat.shouldShowRequestPermissionRationale(
                context as MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )

            if (rationaleRequired) {
                Toast.makeText(context,
                    "Location Permission is required for this feature to work", Toast.LENGTH_LONG)
                    .show()
            } else {
                Toast.makeText(context,
                    "Location Permission is required. Please enable it in the Android Settings", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}