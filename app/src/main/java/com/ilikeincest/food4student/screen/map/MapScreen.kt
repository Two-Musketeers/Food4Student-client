package com.ilikeincest.food4student.screen.map

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.here.sdk.core.GeoCoordinates
import com.ilikeincest.food4student.MainActivity
import com.ilikeincest.food4student.component.MapSearch
import com.ilikeincest.food4student.component.MapViewContainer
import com.ilikeincest.food4student.component.SuggestedAddressList
import com.ilikeincest.food4student.util.LocationUtils
import com.ilikeincest.food4student.viewmodel.LocationViewModel
import com.ilikeincest.food4student.viewmodel.MapViewModel

@Composable
fun MapScreen(
    locationViewModel: LocationViewModel,
    mapViewModel: MapViewModel,
) {
    val context = LocalContext.current
    val locationUtils = LocationUtils(context)

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
                && permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                locationUtils.requestLocationUpdates(locationViewModel)
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
        })

    LaunchedEffect(Unit) {
        if (!locationUtils.hasLocationPermission(context)) {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            locationUtils.requestLocationUpdates(locationViewModel)
        }
    }

    val mapViewInitialized by mapViewModel.mapViewInitialized
    val nearbyPlaces by mapViewModel.nearbyPlaces.collectAsState()
    val location = locationViewModel.location.value

    Column(modifier = Modifier.fillMaxSize()) {
        // Search Bar on Top
        if (mapViewInitialized) {
            MapSearch(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray),
                onSearch = { query -> mapViewModel.autoSuggestExample(query) },
                searchResults = mapViewModel.searchResults,
                onResultClick = { place -> mapViewModel.focusOnPlaceWithMarker(place) }
            )
        }
        Column(modifier = Modifier.fillMaxSize()){
            MapViewContainer(
                mapViewModel = mapViewModel,
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxWidth()
                    .background(Color.Gray)
            )
            Box(modifier = Modifier.fillMaxWidth()
                .weight(0.5f)
                .background(Color.White))
            {
                SuggestedAddressList(
                    nearbyPlaces = nearbyPlaces,
                    onPlaceClick = { place ->
                        place.geoCoordinates?.let { geoCoordinates ->
                            mapViewModel.focusOnPlaceWithMarker(geoCoordinates)
                        }
                    }
                )
            }

        }
    }

    // Focus on the location if available
    LaunchedEffect(location) {
        location?.let {
            val geoCoordinates = GeoCoordinates(it.latitude, it.longitude)
            mapViewModel.focusOnPlaceWithMarker(geoCoordinates)
        }
    }
}