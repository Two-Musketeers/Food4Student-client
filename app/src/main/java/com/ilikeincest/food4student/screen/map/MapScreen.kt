package com.ilikeincest.food4student.screen.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.ilikeincest.food4student.component.MapSearch
import com.ilikeincest.food4student.component.MapViewContainer
import com.ilikeincest.food4student.component.SuggestedAddressList
import com.ilikeincest.food4student.viewmodel.MapViewModel

@Composable
fun MapScreen(
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