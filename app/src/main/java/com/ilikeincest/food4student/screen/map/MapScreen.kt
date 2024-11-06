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
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.ilikeincest.food4student.viewmodel.MapViewModel

@Composable
fun MapScreen(
    mapViewModel: MapViewModel,
) {
    //Request permission for location
    val context = LocalContext.current
    val locationUtils = remember { LocationUtils(context) }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            locationUtils.handlePermissionResult(permissions, mapViewModel)
        }
    )

    LaunchedEffect(Unit) {
        if (!locationUtils.hasLocationPermission(context)) {
            locationUtils.requestLocationPermissions(requestPermissionLauncher)
        } else {
            locationUtils.requestLocationUpdates(mapViewModel)
        }
    }

    //The actual mapScreen
    val mapViewInitialized by mapViewModel.mapViewInitialized
    val nearbyPlaces by mapViewModel.nearbyPlaces.collectAsState()
    val location = mapViewModel.currentLocation.value

    // Focus on the location if available
    LaunchedEffect(location) {
        location?.let {
            val geoCoordinates = GeoCoordinates(it.latitude, it.longitude)
            mapViewModel.focusOnPlaceWithMarker(geoCoordinates)
        }
    }

    Surface {
        Column(modifier = Modifier.fillMaxSize()) {
            // Search Bar on Top
            if (mapViewInitialized) {
                MapSearch(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onSearch = { query -> mapViewModel.autoSuggestExample(query) },
                    searchResults = mapViewModel.searchResults,
                    onResultClick = { place -> mapViewModel.focusOnPlaceWithMarker(place) }
                )
            }
            Column(modifier = Modifier.fillMaxSize()) {
                MapViewContainer(
                    mapViewModel = mapViewModel,
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxWidth()
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.5f)
                )
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
    }
}