package com.ilikeincest.food4student.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.here.sdk.core.GeoCoordinates
import com.here.sdk.mapview.MapMeasure
import com.here.sdk.mapview.MapScheme
import com.here.sdk.mapview.MapView
import com.here.sdk.search.Place
import com.ilikeincest.food4student.util.SearchExample
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MapViewModel : ViewModel() {
    private val _mapView = MutableStateFlow<MapView?>(null)
    val mapView: StateFlow<MapView?> = _mapView

    private val _nearbyPlaces = MutableStateFlow<List<Place>>(emptyList())
    val nearbyPlaces: StateFlow<List<Place>> = _nearbyPlaces

    private val _searchResults = MutableStateFlow<List<Place>>(emptyList())
    val searchResults: StateFlow<List<Place>> = _searchResults

    private var _searchExample: SearchExample? = null
    private val searchExample: SearchExample
        get() = _searchExample ?: throw UninitializedPropertyAccessException("SearchExample has not been initialized")

    fun setMapView(mapView: MapView) {
        _mapView.value = mapView
        _searchExample = SearchExample(mapView.context, mapView).apply {
            onNearbyPlacesFetched = { places ->
                _nearbyPlaces.value = places
            }
        }
    }

    fun autoSuggestExample(query: String) {
        _searchResults.value = searchExample.autoSuggestExample(query)
    }

    fun focusOnPlaceWithMarker(place: Place) {
        searchExample.focusOnPlaceWithMarker(place)
    }
}