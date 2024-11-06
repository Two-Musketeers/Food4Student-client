package com.ilikeincest.food4student.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.here.sdk.core.GeoCoordinates
import com.here.sdk.search.Place

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapSearch(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit,
    searchResults: List<Place>,
    onResultClick: (GeoCoordinates) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var isActive by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(modifier = modifier.fillMaxWidth()
        .padding(horizontal = if (isActive) 0.dp else 16.dp)
        .padding(bottom = 12.dp)
    ) {
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = query,
                    onQueryChange = {
                        query = it
                        onSearch(it)
                    },
                    onSearch = { onSearch(query) },
                    expanded = isActive,
                    onExpandedChange = { active ->
                        isActive = active
                        if (!active) keyboardController?.hide() // Hide keyboard when search bar closes
                    },
                    enabled = true,
                    placeholder = { Text("Tìm vị trí") },
                    leadingIcon = {
                        Icon(
                            imageVector = if (isActive) Icons.AutoMirrored.Filled.ArrowBack else Icons.Default.Search,
                            contentDescription = if (isActive) "Back" else "Search",
                            modifier = Modifier.clickable {
                                if (isActive) {
                                    isActive = false // Collapse search bar
                                    keyboardController?.hide() // Hide keyboard
                                }
                            }
                        )
                    },
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear",
                                modifier = Modifier.clickable {
                                    query = "" // Clear query text
                                }
                            )
                        }
                    },
                    interactionSource = remember { MutableInteractionSource() },
                )
            },
            expanded = isActive,
            onExpandedChange = { active ->
                isActive = active
                if (!active) keyboardController?.hide() // Hide keyboard when search bar closes
            },
            modifier = Modifier.fillMaxWidth(),
            content = {
                if (isActive && query.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        items(searchResults) { place ->
                            Text(
                                text = place.title,
                                modifier = Modifier
                                    .clickable {
                                        place.geoCoordinates?.let { coordinates ->
                                            onResultClick(coordinates)
                                            query = place.title
                                            isActive = false
                                            keyboardController?.hide()
                                        }
                                    }
                                    .padding(16.dp)
                            )
                        }
                    }
                }
            }
        )
    }
}

@Preview
@Composable
fun MapSearchPreview() {
    MapSearch(
        onSearch = {},
        searchResults = emptyList(),
        onResultClick = {}
    )
}