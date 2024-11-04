package com.ilikeincest.food4student.screen.map

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.here.sdk.core.GeoCoordinates
import com.here.sdk.mapview.MapMeasure
import com.here.sdk.mapview.MapScheme
import com.here.sdk.mapview.MapView
import com.ilikeincest.food4student.viewmodel.MapViewModel

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