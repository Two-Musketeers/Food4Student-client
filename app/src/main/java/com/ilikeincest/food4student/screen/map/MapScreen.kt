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
import com.ilikeincest.food4student.component.MapSearch
import com.ilikeincest.food4student.component.SuggestedAddressList
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
                    onPlaceClick = { place -> mapViewModel.focusOnPlaceWithMarker(place) }
                )
            }

        }
    }
}