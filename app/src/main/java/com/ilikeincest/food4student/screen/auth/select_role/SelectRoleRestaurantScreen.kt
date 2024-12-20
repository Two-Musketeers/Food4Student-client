package com.ilikeincest.food4student.screen.auth.select_role

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.ilikeincest.food4student.model.Location

@Composable
fun SelectRoleRestaurantScreen(
    selectedLocation: Location?,
    navToMap: () -> Unit,
) {
    // TODO: show map to visualize where the location is
    var lat by rememberSaveable { mutableStateOf("") }
    var lon by rememberSaveable { mutableStateOf("") }
    LaunchedEffect(selectedLocation) {
        if (selectedLocation == null) return@LaunchedEffect
        lat = selectedLocation.latitude.toString()
        lon = selectedLocation.longitude.toString()
    }
    Scaffold { i ->
        Column(Modifier.padding(i)) {
            TextField(lat, {lat=it})
            TextField(lon, {lon=it})
            Button(onClick = navToMap) {
                Text("asdasd")
            }
        }
    }
}