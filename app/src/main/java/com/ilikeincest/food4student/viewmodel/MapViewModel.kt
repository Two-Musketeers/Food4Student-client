package com.ilikeincest.food4student.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.here.sdk.core.errors.InstantiationErrorException
import com.here.sdk.mapview.MapView
import com.here.sdk.search.Place
import com.here.sdk.search.SearchEngine
import com.ilikeincest.food4student.util.SearchExample
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MapViewModel : ViewModel() {
    lateinit var searchExample: SearchExample

    var mapViewInitialized = mutableStateOf(false)

    private val _nearbyPlaces = MutableStateFlow<List<Place>>(emptyList())
    val nearbyPlaces: StateFlow<List<Place>> = _nearbyPlaces

    private val _searchResults = mutableStateListOf<Place>()
    val searchResults: SnapshotStateList<Place> = _searchResults

    fun setMapViewInitializedTrue() {
        mapViewInitialized.value = true
    }

    fun setNearbyPlaces(places: List<Place>) {
        _nearbyPlaces.value = places
    }

    fun initializeSearchExample(context: Context, mapView: MapView) {
        searchExample = SearchExample(context, mapView).apply {
            onNearbyPlacesFetched = { places ->
                _nearbyPlaces.value = places
            }
        }
    }

    fun autoSuggestExample(query: String) {
        if (!::searchExample.isInitialized) {
            throw UninitializedPropertyAccessException("SearchExample has not been initialized")
        }
        _searchResults.clear()
        Log.d("MapViewModel", "Query: $query")
        _searchResults.addAll(searchExample.autoSuggestExample(query))
    }

    fun focusOnPlaceWithMarker(place: Place) {
        if (!::searchExample.isInitialized) {
            throw UninitializedPropertyAccessException("SearchExample has not been initialized")
        }
        Log.d("MapViewModel", "Hi i work")
        searchExample.focusOnPlaceWithMarker(place)
    }
}