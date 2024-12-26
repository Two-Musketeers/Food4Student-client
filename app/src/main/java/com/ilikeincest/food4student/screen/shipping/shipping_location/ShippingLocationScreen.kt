package com.ilikeincest.food4student.screen.shipping.shipping_location

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ilikeincest.food4student.R
import com.ilikeincest.food4student.component.ErrorDialog
import com.ilikeincest.food4student.component.preview_helper.ScreenPreview
import com.ilikeincest.food4student.model.Location
import com.ilikeincest.food4student.model.SavedShippingLocation
import com.ilikeincest.food4student.screen.shipping.pick_location.component.MapSearchBar
import com.ilikeincest.food4student.screen.shipping.shipping_location.component.AddLocationCard
import com.ilikeincest.food4student.screen.shipping.shipping_location.component.CurrentLocationCard
import com.ilikeincest.food4student.screen.shipping.shipping_location.component.SavedLocationCard
import com.ilikeincest.food4student.model.SavedShippingLocation as ShippingLocation
import com.ilikeincest.food4student.model.SavedShippingLocationType as LocationType

@Composable
fun ShippingLocationScreen(
    pickedLocation: Location?,
    onNavigateUp: () -> Unit,
    onPickFromMap: () -> Unit,
    onAddLocation: (type: LocationType) -> Unit,
    onEditLocation: (id: String) -> Unit,
    vm: ShippingLocationViewModel = hiltViewModel()
) {
    val locationList = vm.locationList
    val context = LocalContext.current
    LaunchedEffect(pickedLocation) {
        if (pickedLocation == null) return@LaunchedEffect
        vm.pickLocation(pickedLocation, context)
    }
    LaunchedEffect(Unit) {
        vm.fetchCurrentFromDStore(context)
        vm.reloadLocationList()
    }
    var error by vm.error
    if (error.isNotBlank()) {
        ErrorDialog(
            message = error,
            onDismiss = { error = "" },
        )
    }
    val location by vm.currentLocation.collectAsState()
    ShippingLocationScreenContent(
        locationList = locationList,
        onNavigateUp = onNavigateUp,
        onAddLocation = onAddLocation,
        onPickFromMap = onPickFromMap,
        onEditLocation = onEditLocation,
        currentLocation = location.location,
        currentAddress = location.address,
        onPickLocation = { vm.setCurrent(it, context) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ShippingLocationScreenContent(
    currentLocation: String,
    currentAddress: String,
    locationList: List<ShippingLocation>,
    onNavigateUp: () -> Unit,
    onAddLocation: (type: LocationType) -> Unit,
    onPickFromMap: () -> Unit,
    onEditLocation: (id: String) -> Unit,
    onPickLocation: (SavedShippingLocation) -> Unit,
) {
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
                    onEditCurrent = { onEditLocation("") }, // empty id is current
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
                        receiverName = location.name,
                        receiverPhone = location.phoneNumber,
                        onSelected = { onPickLocation(location) },
                        onEditLocation = { onEditLocation(location.id) },
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
                        onClick = { onAddLocation(LocationType.Home) },
                    )
                }
                if (!workLocationSaved) {
                    AddLocationCard(
                        locationType = LocationType.Work,
                        onClick = { onAddLocation(LocationType.Work) },
                    )
                }
                AddLocationCard(
                    locationType = LocationType.Other,
                    onClick = { onAddLocation(LocationType.Other) },
                )
            }
        }
    }
}

@Preview
@Composable
private fun Prev() { ScreenPreview {
    ShippingLocationScreenContent(
        currentLocation = "KTX Đại học Quốc gia TPHCM - Khu B",
        currentAddress = "15/12/564/23 Tô Vĩnh Diện, Phường Đông Hòa, Dĩ An, Bình Dương",
        listOf(
            ShippingLocation(
                "",
                locationType = LocationType.Home,
                buildingNote = "Cổng trước",
                location = "KTX Đại học Quốc gia TPHCM - Khu B",
                address = "15 Tô Vĩnh Diện, Phường Đông Hòa, Dĩ An, Bình Dương",
                name = "Hồ Nguyên Minh",
                phoneNumber = "01234567879",
                latitude = 0.0, longitude = 0.0,
            ),
            ShippingLocation(
                "",
                locationType = LocationType.Work,
                buildingNote = "Cổng trước",
                location = "KTX Đại học Quốc gia TPHCM - Khu B",
                address = "15 Tô Vĩnh Diện, Phường Đông Hòa, Dĩ An, Bình Dương",
                name = "Hồ Nguyên Minh",
                phoneNumber = "01234567879",
                latitude = 0.0, longitude = 0.0,
            ),
            ShippingLocation(
                "",
                locationType = LocationType.Other,
                otherLocationTypeTitle = "Dating location",
                location = "Trường mẫu giáo Tư thục Sao Mai",
                address = "Lmao u believe me fr?",
                name = "Hứa Văn Lý",
                phoneNumber = "0123456789",
                latitude = 0.0, longitude = 0.0,
            )
        ),
        {}, {}, {}, {}, {}
    )
} }

@Preview
@Composable
private fun PrevEmpty() { ScreenPreview {
    ShippingLocationScreenContent(
        currentLocation = "KTX Đại học Quốc gia TPHCM - Khu B",
        currentAddress = "15/12/564/23 Tô Vĩnh Diện, Phường Đông Hòa, Dĩ An, Bình Dương",
        listOf(), {}, {}, {}, {}, {}
    )
} }

@Preview
@Composable
private fun PrevPartial() { ScreenPreview {
    ShippingLocationScreenContent(
        currentLocation = "KTX Đại học Quốc gia TPHCM - Khu B",
        currentAddress = "15/12/564/23 Tô Vĩnh Diện, Phường Đông Hòa, Dĩ An, Bình Dương",
        listOf(ShippingLocation(
            "",
            locationType = LocationType.Work,
            buildingNote = "Cổng trước",
            location = "KTX Đại học Quốc gia TPHCM - Khu B",
            address = "15 Tô Vĩnh Diện, Phường Đông Hòa, Dĩ An, Bình Dương",
            name = "Hồ Nguyên Minh",
            phoneNumber = "01234567879",
            latitude = 0.0, longitude = 0.0,
        ),),
        {}, {}, {}, {}, {}
    )
} }