package com.ilikeincest.food4student.screen.shipping_location

import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.ilikeincest.food4student.R
import com.ilikeincest.food4student.component.preview_helper.ScreenPreview
import com.ilikeincest.food4student.screen.map.component.MapSearchBar
import com.ilikeincest.food4student.screen.map.component.MapViewContainer
import com.ilikeincest.food4student.screen.map.component.SuggestedAddressList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShippingLocationScreen() {

    var expanded by remember { mutableStateOf(false) }
    val animatedOffset by animateIntOffsetAsState(
        targetValue = if (expanded) IntOffset(0, 100) else IntOffset(0, 0),
        label = "Search bar expanded content offset"
    )
    val density = LocalDensity.current
    val animateTopBarOffset by animateIntOffsetAsState(
        targetValue = if (expanded) IntOffset(0, -300) else IntOffset(0, 0),
        label = "Top bar move up"
    )

    Scaffold(
        topBar = { CenterAlignedTopAppBar(
            title = { Text("Địa chỉ giao hàng") },
            navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "back")
                }
            },
            actions = {
                IconButton(onClick = {}) {
                    Icon(painterResource(R.drawable.map_search), "Pick location on map")
                }
            },
            modifier = Modifier.absoluteOffset { animateTopBarOffset }
        ) }
    ) { innerPadding ->
        Box(Modifier.fillMaxSize()) {
            // Search bar on top
            val topBarHeight = innerPadding.calculateTopPadding() -
                    WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
            var topBarHeightPx: Int
            with(density) {
                topBarHeightPx = topBarHeight.roundToPx()
            }
            val searchBarOffset by animateIntOffsetAsState(
                targetValue = if (expanded) IntOffset(0, 0) else IntOffset(0, topBarHeightPx),
                label = "Push searchbar up when expanded"
            )
            // TODO
            MapSearchBar(
                onSearch = { query -> },
                searchResults = listOf(),
                onResultClick = { place -> },
                onExpandedChange = { newValue ->
                    expanded = newValue
                },
                modifier = Modifier.align(Alignment.TopCenter).absoluteOffset { searchBarOffset }
            )

            Column(Modifier.fillMaxSize().padding(innerPadding).absoluteOffset { animatedOffset }) {
                // Placeholder for the search bar
                Spacer(Modifier.height(78.dp))


            }
        }
    }
}

@Preview
@Composable
private fun Prev() { ScreenPreview {
    ShippingLocationScreen()
} }