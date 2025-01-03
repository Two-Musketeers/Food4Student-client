package com.ilikeincest.food4student.screen.shipping.pick_location.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.here.sdk.core.GeoCoordinates
import com.here.sdk.search.Place
import com.ilikeincest.food4student.DEBOUNCE_DELAY
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MapSearchBar(
    onSearch: (String) -> Unit,
    searchResults: List<Place>,
    onResultClick: (GeoCoordinates) -> Unit,
    modifier: Modifier = Modifier,
    onExpandedChange: (Boolean) -> Unit = {}
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

    LaunchedEffect(expanded) {
        onExpandedChange(expanded)
    }

    val searchBarPadding by animateDpAsState(
        targetValue = if(expanded) 0.dp else 16.dp,
        label = "Map search bar padding"
    )

    SearchBar(
        modifier = modifier
//            .offset(y = (-8).dp)
            .fillMaxWidth()
            .padding(horizontal = searchBarPadding),
        inputField = { SearchBarDefaults.InputField(
            query = query,
            onQueryChange = { query = it },
            onSearch = { onSearch(query) },
            expanded = expanded,
            onExpandedChange = { newValue ->
                expanded = newValue
                query = "" // Clear query text when search bar expands
                if (!newValue) keyboardController?.hide() // Hide keyboard when search bar closes
            },
            placeholder = { Text("Tìm vị trí") },
            leadingIcon = {
                if (expanded) {
                    IconButton(onClick = {
                        expanded = false // Collapse search bar
                        keyboardController?.hide() // Hide keyboard
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                } else {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                }
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

            val state = rememberLazyListState()
            val isScrolledToTop by remember { derivedStateOf {
                state.firstVisibleItemIndex == 0
                        && state.firstVisibleItemScrollOffset == 0
            } }
            val ime = LocalSoftwareKeyboardController.current
            LaunchedEffect(isScrolledToTop) {
                if (isScrolledToTop) ime?.show()
                else ime?.hide()
            }
            LazyColumn(
                state = state,
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

@Preview
@Composable
fun MapSearchPreview() {
    MapSearchBar(
        onSearch = {},
        searchResults = emptyList(),
        onResultClick = {}
    )
}