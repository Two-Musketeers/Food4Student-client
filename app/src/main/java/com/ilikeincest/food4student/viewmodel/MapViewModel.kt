package com.ilikeincest.food4student.viewmodel

import android.content.Context
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

class MapViewModel : ViewModel() {
    private val _mapView = MutableStateFlow<MapView?>(null)
    val mapView: StateFlow<MapView?> = _mapView

    private val _mapViewInitialized = MutableStateFlow(false)
    val mapViewInitialized: StateFlow<Boolean> = _mapViewInitialized

    private val _nearbyPlaces = MutableStateFlow<List<Place>>(emptyList())
    val nearbyPlaces: StateFlow<List<Place>> = _nearbyPlaces

    private val _searchResults = MutableStateFlow<List<Place>>(emptyList())
    val searchResults: StateFlow<List<Place>> = _searchResults

    lateinit var searchExample: SearchExample

    fun setMapView(mapView: MapView) {
        _mapView.value = mapView
    }

    fun initializeMapView(mapView: MapView, context: Context) {
        searchExample = SearchExample(context, mapView).apply {
            onNearbyPlacesFetched = { places ->
                _nearbyPlaces.value = places
            }
        }
        mapView.loadMapScene {
            _mapViewInitialized.value = true
        }
    }

    fun autoSuggestExample(query: String) {
        searchExample.autoSuggestExample(query)
    }

    fun focusOnPlaceWithMarker(place: Place) {
        searchExample.focusOnPlaceWithMarker(place)
    }

    fun setMapInitialized(initialized: Boolean) {
        _mapViewInitialized.value = initialized
    }
}