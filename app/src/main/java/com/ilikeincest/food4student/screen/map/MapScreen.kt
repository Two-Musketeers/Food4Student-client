package com.ilikeincest.food4student.screen.map

import android.content.Context
import android.view.ViewGroup
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.here.sdk.core.GeoCoordinates
import com.here.sdk.core.engine.SDKNativeEngine
import com.here.sdk.core.engine.SDKOptions
import com.here.sdk.core.errors.InstantiationErrorException
import com.here.sdk.mapview.MapMeasure
import com.here.sdk.mapview.MapScheme
import com.here.sdk.mapview.MapView
import com.here.sdk.search.Place
import com.ilikeincest.food4student.BuildConfig
import com.ilikeincest.food4student.initializer.HereInitializer
import com.ilikeincest.food4student.util.SearchExample
import com.ilikeincest.food4student.viewmodel.MapViewModel

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    mapViewModel: MapViewModel
) {
    val mapViewInitialized by mapViewModel.mapViewInitialized
    val nearbyPlaces by mapViewModel.nearbyPlaces.collectAsState()

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

        // MapViewContainer taking up half the available space
        MapViewContainer(
            mapViewModel = mapViewModel,
            modifier = Modifier
                .weight(0.5f)
                .fillMaxWidth()
                .background(Color.Gray)
        )

        // SuggestedAddressList taking up the other half
        SuggestedAddressList(
            modifier = Modifier
                .weight(0.5f)
                .fillMaxWidth()
                .background(Color.White),
            nearbyPlaces = nearbyPlaces,
            onPlaceClick = { place -> mapViewModel.focusOnPlaceWithMarker(place) }
        )
    }
}

@Composable
fun MapViewContainer(
    modifier: Modifier = Modifier,
    mapViewModel: MapViewModel
) {
    AndroidView(
        factory = { context ->
            MapView(context).apply {
                onCreate(null)
                onResume()
                loadMapScene {
                    mapViewModel.setMapViewInitializedTrue()
                    mapViewModel.initializeSearchExample(context, this)
                }
            }
        },
        modifier = modifier
    )
}

fun MapView.loadMapScene(onMapLoaded: () -> Unit) {
    val distanceInMeters = 1000 * 10
    val mapMeasureZoom = MapMeasure(MapMeasure.Kind.DISTANCE, distanceInMeters.toDouble())
    this.camera.lookAt(GeoCoordinates(52.530932, 13.384915), mapMeasureZoom)

    this.mapScene.loadScene(MapScheme.NORMAL_DAY) { mapError ->
        mapError?.let {
            // Handle map loading error
        } ?: onMapLoaded()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapSearch(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit,
    searchResults: List<Place>,
    onResultClick: (Place) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var isActive by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(modifier = modifier.fillMaxWidth()) {
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = query,
                    onQueryChange = {
                        query = it
                        onSearch(it)
                    },
                    onSearch = { onSearch(query) },
                    expanded = isActive,
                    onExpandedChange = { active ->
                        isActive = active
                        if (!active) keyboardController?.hide() // Hide keyboard when search bar closes
                    },
                    enabled = true,
                    placeholder = { Text("Search") },
                    leadingIcon = {
                        Icon(
                            imageVector = if (isActive) Icons.Default.ArrowBack else Icons.Default.Search,
                            contentDescription = if (isActive) "Back" else "Search",
                            modifier = Modifier.clickable {
                                if (isActive) {
                                    isActive = false // Collapse search bar
                                    keyboardController?.hide() // Hide keyboard
                                }
                            }
                        )
                    },
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear",
                                modifier = Modifier.clickable {
                                    query = "" // Clear query text
                                }
                            )
                        }
                    },
                    interactionSource = remember { MutableInteractionSource() }
                )
            },
            expanded = isActive,
            onExpandedChange = { active ->
                isActive = active
                if (!active) keyboardController?.hide() // Hide keyboard when search bar closes
            },
            modifier = Modifier.fillMaxWidth(),
            content = {
                if (isActive && query.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                    ) {
                        items(searchResults) { place ->
                            Text(
                                text = place.title,
                                modifier = Modifier
                                    .clickable {
                                        onResultClick(place)
                                        query = place.title
                                        isActive = false
                                        keyboardController?.hide()
                                    }
                                    .padding(16.dp)
                            )
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun SuggestedAddressList(
    modifier: Modifier,
    nearbyPlaces: List<Place>,
    onPlaceClick: (Place) -> Unit
) {
    Column {
        Text(
            text = "Địa chỉ gợi ý",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            items(nearbyPlaces) { place ->
                ListItem(
                    headlineContent = { Text(text = place.title) },
                    supportingContent = { Text(text = place.address?.addressText ?: "") },
                    modifier = Modifier
                        .clickable {
                            onPlaceClick(place)
                        }
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                )
                HorizontalDivider()
            }
        }
    }
}