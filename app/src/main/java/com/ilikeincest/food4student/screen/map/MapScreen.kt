package com.ilikeincest.food4student.screen.map

import android.content.Context
import android.view.ViewGroup
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    mapViewModel: MapViewModel = viewModel()
) {
    val context = LocalContext.current
    val mapViewInitialized by mapViewModel.mapViewInitialized.collectAsState()
    val nearbyPlaces by mapViewModel.nearbyPlaces.collectAsState()
    val searchResults by mapViewModel.searchResults.collectAsState()

    fun initializeHERESDK() {
        val accessKeyID = BuildConfig.HERE_API_KEY
        val accessKeySecret = BuildConfig.HERE_API_SECRET_KEY
        val options = SDKOptions(accessKeyID, accessKeySecret)
        try {
            SDKNativeEngine.makeSharedInstance(context, options)
        } catch (e: InstantiationErrorException) {
            throw RuntimeException("Initialization of HERE SDK failed: " + e.error.name)
        }
    }

    initializeHERESDK()

    Box(modifier = modifier.fillMaxSize()) {
        MapViewContainer(
            mapViewModel = mapViewModel,
            modifier = Modifier.fillMaxSize(),
        )
        if (mapViewInitialized) {
            MapSearch(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .background(Color.White),
                onSearch = { query -> mapViewModel.autoSuggestExample(query) },
                searchResults = searchResults,
                onResultClick = { place -> mapViewModel.focusOnPlaceWithMarker(place) }
            )
        }
        SuggestedAddressList(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
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
    val context = LocalContext.current
    var mapViewInitialized by remember { mutableStateOf(false) }
    var nearbyPlaces by remember { mutableStateOf<List<Place>>(emptyList()) }
    lateinit var searchExample: SearchExample

    AndroidView(
        factory = { context ->
            MapView(context).apply {
                onCreate(null)
                onResume()
                loadMapScene {
                    mapViewInitialized = true
                    searchExample = SearchExample(context, this).apply {
                        onNearbyPlacesFetched = { places ->
                            nearbyPlaces = places
                        }
                    }
                    mapViewModel.setMapView(this)
                }
            }
        },
        modifier = Modifier
            .fillMaxSize()
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
        // Default expandable Search Bar without height modifications
        SearchBar(
            query = query,
            onQueryChange = {
                query = it
                onSearch(it)
            },
            onSearch = { onSearch(query) },
            placeholder = { Text("Search") },
            modifier = Modifier.fillMaxWidth(),
            active = isActive,
            onActiveChange = { active ->
                isActive = active
                if (!active) keyboardController?.hide() // Hide keyboard when search bar closes
            },
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
            content = {
                if (isActive && query.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(searchResults) { place ->
                            Text(
                                text = place.title,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White)
                                    .clickable {
                                        onResultClick(place)
                                        query = place.title // Set search bar text to selected suggestion
                                        isActive = false // Collapse search bar
                                        keyboardController?.hide() // Hide keyboard
                                    }
                                    .padding(12.dp)
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
        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)) {
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