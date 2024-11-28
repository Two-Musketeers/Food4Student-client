package com.ilikeincest.food4student.screen.shipping_location

import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.ilikeincest.food4student.R
import com.ilikeincest.food4student.component.preview_helper.ScreenPreview
import com.ilikeincest.food4student.screen.map.component.MapSearchBar
import com.ilikeincest.food4student.screen.shipping_location.component.AddLocationCard
import com.ilikeincest.food4student.screen.shipping_location.component.CurrentLocationCard
import com.ilikeincest.food4student.screen.shipping_location.component.SavedLocationCard
import com.ilikeincest.food4student.model.SavedShippingLocation as Location
import com.ilikeincest.food4student.model.SavedShippingLocationType as LocationType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShippingLocationScreen(
    locationList: List<Location>,
    onNavigateUp: () -> Unit,
    onPickFromMap: () -> Unit,
) {
    val currentLocation = "KTX Đại học Quốc gia TPHCM - Khu B"
    val currentAddress = "15/12/564/23 Tô Vĩnh Diện, Phường Đông Hòa, Dĩ An, Bình Dương"

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
                IconButton(onClick = onNavigateUp) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "back")
                }
            },
            actions = {
                IconButton(onClick = onPickFromMap) {
                    Icon(painterResource(R.drawable.map_search), "Pick location on map")
                }
            },
            modifier = Modifier.absoluteOffset { animateTopBarOffset }
        ) }
    ) { innerPadding ->
        Box(Modifier.fillMaxSize()) {
            // Search bar on top
            val statusBarPadding = WindowInsets.statusBars.asPaddingValues()
            val topBarHeight = remember {
                innerPadding.calculateTopPadding() - statusBarPadding.calculateTopPadding()
            }
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
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .absoluteOffset { searchBarOffset }
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .padding(top = 78.dp) // space out the search bar
                    .absoluteOffset { animatedOffset }
                    .verticalScroll(rememberScrollState())
            ) {
                CurrentLocationCard(
                    currentLocation = currentLocation,
                    locationAddress = currentAddress,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                HorizontalDivider(Modifier.padding(horizontal = 24.dp))
                Text("Địa chỉ giao hàng đã lưu",
                    style = typography.titleMedium,
                    modifier = Modifier.padding(vertical = 6.dp)
                )

                var homeLocationSaved = false
                var workLocationSaved = false

                for (location in locationList) {
                    if (location.locationType == LocationType.Home)
                        homeLocationSaved = true
                    if (location.locationType == LocationType.Work)
                        workLocationSaved = true
                    SavedLocationCard(
                        locationType = location.locationType,
                        location = location.location,
                        address = location.address,
                        receiverName = location.receiverName,
                        receiverPhone = location.receiverPhone,
                        onSelected = {}, // TODO
                        onEditLocation = {}, // TODO
                        buildingNote = location.buildingNote,
                        otherLocationTypeTitle = location.otherLocationTypeTitle
                    )
                }

                if (locationList.isNotEmpty()) {
                    HorizontalDivider(Modifier.padding(horizontal = 24.dp))
                }

                if (!homeLocationSaved) {
                    AddLocationCard(
                        locationType = LocationType.Home,
                        onClick = {},
                    )
                }
                if (!workLocationSaved) {
                    AddLocationCard(
                        locationType = LocationType.Work,
                        onClick = {},
                    )
                }
                AddLocationCard(
                    locationType = LocationType.Other,
                    onClick = {},
                )
            }
        }
    }
}

@Preview
@Composable
private fun Prev() { ScreenPreview {
    ShippingLocationScreen(listOf(
        Location(
            locationType = LocationType.Home,
            buildingNote = "Cổng trước",
            location = "KTX Đại học Quốc gia TPHCM - Khu B",
            address = "15 Tô Vĩnh Diện, Phường Đông Hòa, Dĩ An, Bình Dương",
            receiverName = "Hồ Nguyên Minh",
            receiverPhone = "01234567879",
        ),
        Location(
            locationType = LocationType.Work,
            buildingNote = "Cổng trước",
            location = "KTX Đại học Quốc gia TPHCM - Khu B",
            address = "15 Tô Vĩnh Diện, Phường Đông Hòa, Dĩ An, Bình Dương",
            receiverName = "Hồ Nguyên Minh",
            receiverPhone = "01234567879",
        ),
        Location(
            locationType = LocationType.Other,
            otherLocationTypeTitle = "Dating location",
            location = "Trường mẫu giáo Tư thục Sao Mai",
            address = "Lmao u believe me fr?",
            receiverName = "Hứa Văn Lý",
            receiverPhone = "0123456789",
        )
    ), {}, {})
} }

@Preview
@Composable
private fun PrevEmpty() { ScreenPreview {
    ShippingLocationScreen(listOf(), {}, {})
} }

@Preview
@Composable
private fun PrevPartial() { ScreenPreview {
    ShippingLocationScreen(listOf(
        Location(
            locationType = LocationType.Work,
            buildingNote = "Cổng trước",
            location = "KTX Đại học Quốc gia TPHCM - Khu B",
            address = "15 Tô Vĩnh Diện, Phường Đông Hòa, Dĩ An, Bình Dương",
            receiverName = "Hồ Nguyên Minh",
            receiverPhone = "01234567879",
        ),
    ), {}, {})
} }