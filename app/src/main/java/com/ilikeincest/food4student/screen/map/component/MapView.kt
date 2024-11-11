package com.ilikeincest.food4student.screen.map.component

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.here.sdk.core.Anchor2D
import com.here.sdk.core.GeoCoordinates
import com.here.sdk.core.Point2D
import com.here.sdk.mapview.MapMeasure
import com.here.sdk.mapview.MapScheme
import com.here.sdk.mapview.MapView
import com.ilikeincest.food4student.viewmodel.MapViewModel

@Composable
fun MapViewContainer(
    modifier: Modifier = Modifier,
    mapViewModel: MapViewModel
) {
    val currentTheme = isSystemInDarkTheme()
    var isDarkTheme by remember { mutableStateOf(currentTheme) }

    SideEffect {
        if (currentTheme != isDarkTheme) {
            isDarkTheme = currentTheme
        }
    }

    AndroidView(
        factory = { context ->
            MapView(context).apply {
                onCreate(null)
                setWatermarkLocation(Anchor2D(0.0, 0.0), Point2D())
                onResume()
                loadMapScene {
                    mapViewModel.setMapViewInitializedTrue()
                    mapViewModel.initializeSearchExample(context, this)
                }

            }
        },
        update = { mapView ->
            mapView.onResume()
            // get system theme and set map scheme
            val mapScheme = if (isDarkTheme) MapScheme.NORMAL_NIGHT else MapScheme.NORMAL_DAY
            mapView.mapScene.loadScene(mapScheme) {} // TODO: handle error
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