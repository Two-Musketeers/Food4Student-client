package com.ilikeincest.food4student.screen.map.component

import androidx.compose.runtime.Composable
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
        update = { mapView ->
            mapView.onResume()
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