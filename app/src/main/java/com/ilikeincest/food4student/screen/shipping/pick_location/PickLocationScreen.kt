package com.ilikeincest.food4student.screen.shipping.pick_location

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ilikeincest.food4student.R
import com.ilikeincest.food4student.model.Location
import com.ilikeincest.food4student.screen.shipping.pick_location.component.MapSearchBar
import com.ilikeincest.food4student.screen.shipping.pick_location.component.MapViewContainer
import com.ilikeincest.food4student.screen.shipping.pick_location.component.SuggestedAddressList
import com.ilikeincest.food4student.util.LocationUtils
import com.ilikeincest.food4student.viewmodel.MapViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    onNavigateUp: () -> Unit,
    onSelectLocation: (Location) -> Unit,
    mapViewModel: MapViewModel = viewModel(),
) {
    //Request permission for location
    val context = LocalContext.current
    // TODO: refactor this shit lmao
    val locationUtils = rememberSaveable { LocationUtils(context) }

    var hasLocationPermission by remember { mutableStateOf(false) }
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
                && permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                hasLocationPermission = true
            }
            locationUtils.handlePermissionResult(permissions, mapViewModel)
            locationUtils.requestLocationOnce(mapViewModel)
        }
    )

    DisposableEffect(Unit) {
        if (!locationUtils.hasLocationPermission(context)) {
            locationUtils.requestLocationPermissions(requestPermissionLauncher)
        } else {
            hasLocationPermission = true
        }
        onDispose {
            locationUtils.stopLocationUpdates()
        }
    }

    //The actual mapScreen
    val mapViewInitialized by mapViewModel.mapViewInitialized
    val nearbyPlaces by mapViewModel.nearbyPlaces.collectAsState()

    LaunchedEffect(mapViewInitialized, hasLocationPermission) {
        if (!mapViewInitialized) return@LaunchedEffect
        if (!hasLocationPermission) return@LaunchedEffect
        locationUtils.requestLocationOnce(mapViewModel)
    }

    var expanded by remember { mutableStateOf(false) }
    val animatedOffset by animateIntOffsetAsState(
        targetValue = if (expanded) IntOffset(0, 100) else IntOffset(0, 0),
        label = "Search bar expanded content offset"
    )
    val density = LocalDensity.current
    val animateTopBarOffset by animateIntOffsetAsState(
        targetValue = if (expanded) IntOffset(0, -300) else IntOffset(0, 0),
        label = "Top bar move up"
    )

    Scaffold(
        topBar = { TopAppBar(
            title = { Text("Chọn địa chỉ") },
            navigationIcon = { IconButton(onNavigateUp) {
                Icon(Icons.AutoMirrored.Default.ArrowBack, "back")
            } },
            actions = {
                FilledTonalButton(
                    contentPadding = PaddingValues(start = 16.dp, top = 10.dp, end = 24.dp, bottom = 10.dp),
                    onClick = {
                        val coord = mapViewModel.mapCenterCoord() ?: return@FilledTonalButton
                        onSelectLocation(
                            Location(
                                latitude = coord.latitude,
                                longitude = coord.longitude,
                                address = nearbyPlaces[0].address.addressText
                            )
                        )
                    }
                ) {
                    Icon(Icons.Default.Check, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Chọn")
                }
                Spacer(Modifier.width(12.dp))
            },
            modifier = Modifier.absoluteOffset { animateTopBarOffset }
        ) }
    ) { Box(Modifier.fillMaxSize()) {
        // Search bar on top
        val topBarHeight = it.calculateTopPadding() -
                WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
        var topBarHeightPx: Int
        with(density) {
            topBarHeightPx = topBarHeight.roundToPx()
        }
        val searchBarOffset by animateIntOffsetAsState(
            targetValue = if (expanded) IntOffset(0, 0) else IntOffset(0, topBarHeightPx),
            label = "Push searchbar up when expanded"
        )
        if (mapViewInitialized) {
            MapSearchBar(
                onSearch = { query -> mapViewModel.autoSuggestExample(query) },
                searchResults = mapViewModel.searchResults,
                onResultClick = { place ->
                    mapViewModel.focusOnPlaceWithMarker(place)
                },
                onExpandedChange = { newValue ->
                    expanded = newValue
                },
                modifier = Modifier.align(Alignment.TopCenter).absoluteOffset { searchBarOffset }
            )
        }

        Column(Modifier.fillMaxSize().padding(it).absoluteOffset { animatedOffset }) {
            // Placeholder for the search bar
            Spacer(Modifier.height(76.dp))
            Box(
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxWidth()
            ) {
                MapViewContainer(
                    mapViewModel = mapViewModel
                )
                // point of interest icon
                Icon(
                    painterResource(R.drawable.poi),
                    null, tint = Color.Unspecified,
                    modifier = Modifier.align(Alignment.Center).size(40.dp)
                )
                // Current location button
                if (hasLocationPermission) {
                    FilledIconButton(
                        onClick = { locationUtils.requestLocationOnce(mapViewModel) },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                            .size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = "Go to current location"
                        )
                    }
                }
            }
            SuggestedAddressList(
                nearbyPlaces = nearbyPlaces,
                onPlaceClick = { place ->
                    place.geoCoordinates?.let { geoCoordinates ->
                        mapViewModel.focusOnPlaceWithMarker(geoCoordinates)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f)
            )
        }
    } }
}