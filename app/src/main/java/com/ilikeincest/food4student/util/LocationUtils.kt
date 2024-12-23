package com.ilikeincest.food4student.util
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import android.os.Parcelable
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.here.sdk.core.GeoCoordinates
import com.ilikeincest.food4student.MainActivity
import com.ilikeincest.food4student.viewmodel.MapViewModel
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.RawValue
import kotlinx.parcelize.Parcelize

@Parcelize
class LocationUtils(val context: @RawValue Context) : Parcelable {
    @IgnoredOnParcel
    private val _fusedLocationClient: FusedLocationProviderClient
            = LocationServices.getFusedLocationProviderClient(context)

    @IgnoredOnParcel
    private val _locationCallback = object : LocationCallback() {}
    @SuppressLint("MissingPermission")
    fun requestLocationUpdates() {
        val locationRequest = LocationRequest
            .Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
            .build()

        _fusedLocationClient.requestLocationUpdates(locationRequest, _locationCallback, Looper.getMainLooper())
    }

    fun stopLocationUpdates() {
        _fusedLocationClient.removeLocationUpdates(_locationCallback)
    }

    @SuppressLint("MissingPermission")
    fun requestLocationOnce(viewModel: MapViewModel) {
        _fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val currentLocation = GeoCoordinates(it.latitude, it.longitude)
                viewModel.focusOnPlaceWithMarker(currentLocation)
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun requestLocationForLatLong(onLocationReceived: (GeoCoordinates) -> Unit) {
        _fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val currentLocation = GeoCoordinates(it.latitude, it.longitude)
                onLocationReceived(currentLocation)
            }
        }
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
    ) {
        // TODO refactor and switch to dialog
        if (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            && permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
            requestLocationUpdates()
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