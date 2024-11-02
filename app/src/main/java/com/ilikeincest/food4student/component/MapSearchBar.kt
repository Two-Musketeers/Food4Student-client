package com.ilikeincest.food4student.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.here.sdk.search.Place


//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MapSearch(
//    modifier: Modifier = Modifier,
//    mapViewModel: MapViewModel,
//    onResultClick: (Place) -> Unit
//) {
//    var query by remember { mutableStateOf("") }
//    var isActive by remember { mutableStateOf(false) }
//    val keyboardController = LocalSoftwareKeyboardController.current
//
//    Box(modifier = modifier.fillMaxWidth()) {
//        SearchBar(
//            query = query,
//            onQueryChange = {
//                query = it
//                mapViewModel.autoSuggestExample(it)
//            },
//            onSearch = { mapViewModel.autoSuggestExample(query) },
//            placeholder = { Text("Search") },
//            modifier = Modifier.fillMaxWidth(),
//            active = isActive,
//            onActiveChange = { active ->
//                isActive = active
//                if (!active) keyboardController?.hide()
//            },
//            leadingIcon = {
//                Icon(
//                    imageVector = if (isActive) Icons.Default.ArrowBack else Icons.Default.Search,
//                    contentDescription = if (isActive) "Back" else "Search",
//                    modifier = Modifier.clickable {
//                        if (isActive) {
//                            isActive = false
//                            keyboardController?.hide()
//                        }
//                    }
//                )
//            },
//            trailingIcon = {
//                if (query.isNotEmpty()) {
//                    Icon(
//                        imageVector = Icons.Default.Clear,
//                        contentDescription = "Clear",
//                        modifier = Modifier.clickable {
//                            query = ""
//                        }
//                    )
//                }
//            },
//            content = {
//                if (isActive && query.isNotEmpty()) {
//                    LazyColumn(
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        items(mapViewModel.searchResults) { place ->
//                            Text(
//                                text = place.title,
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .background(Color.White)
//                                    .clickable {
//                                        onResultClick(place)
//                                        query = place.title
//                                        isActive = false
//                                        keyboardController?.hide()
//                                    }
//                                    .padding(12.dp)
//                            )
//                        }
//                    }
//                }
//            }
//        )
//    }
//}