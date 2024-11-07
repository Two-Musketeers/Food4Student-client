package com.ilikeincest.food4student.screen.map.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.here.sdk.core.GeoCoordinates
import com.here.sdk.search.Place
import kotlinx.coroutines.delay

private const val DEBOUNCE_DELAY = 200L

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapSearchBar(
    onSearch: (String) -> Unit,
    searchResults: List<Place>,
    onResultClick: (GeoCoordinates) -> Unit,
    modifier: Modifier = Modifier,
) {
    var query by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    // query search debounce
    LaunchedEffect(query) {
        if (query.isBlank()) return@LaunchedEffect
        delay(DEBOUNCE_DELAY)
        onSearch(query)
    }

    Box(modifier.fillMaxWidth()
        .semantics { isTraversalGroup = true }
        .padding(bottom = 12.dp)
    ) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .semantics { traversalIndex = 0f },
            inputField = { SearchBarDefaults.InputField(
                query = query,
                onQueryChange = {
                    query = it
                },
                onSearch = { onSearch(query) },
                expanded = expanded,
                onExpandedChange = { active ->
                    expanded = active
                    query = "" // Clear query text when search bar expands
                    if (!active) keyboardController?.hide() // Hide keyboard when search bar closes
                },
                placeholder = { Text("Tìm vị trí") },
                leadingIcon = {
                    Icon(
                        imageVector =
                            if (expanded) Icons.AutoMirrored.Filled.ArrowBack
                            else Icons.Default.Search,
                        contentDescription = if (expanded) "Back" else "Search",
                        modifier = Modifier.clickable {
                            if (expanded) {
                                expanded = false // Collapse search bar
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
            ) },
            expanded = expanded,
            onExpandedChange = { active ->
                expanded = active
                if (!active) keyboardController?.hide() // Hide keyboard when search bar closes
            },
            content = {
                if (!expanded || query.isEmpty())
                    return@SearchBar

                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) { items(searchResults) { place ->
                    Text(
                        text = place.title,
                        modifier = Modifier
                            .clickable {
                                place.geoCoordinates?.let { coordinates ->
                                    onResultClick(coordinates)
                                    query = place.title
                                    expanded = false
                                    keyboardController?.hide()
                                }
                            }
                            .padding(16.dp)
                    )
                } }
            }
        )
    }
}

@Preview
@Composable
fun MapSearchPreview() {
    MapSearchBar(
        onSearch = {},
        searchResults = emptyList(),
        onResultClick = {}
    )
}