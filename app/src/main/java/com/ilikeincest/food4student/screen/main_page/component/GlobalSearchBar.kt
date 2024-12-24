package com.ilikeincest.food4student.screen.main_page.component

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ilikeincest.food4student.component.AsyncImageOrMonogram
import com.ilikeincest.food4student.component.preview_helper.ComponentPreview
import com.ilikeincest.food4student.dto.NoNeedToFetchAgainBuddy
import com.ilikeincest.food4student.screen.main_page.MainScreenViewModel

/**
 * A global search bar to be used in main screens
 * Look at MapSearchBar and MapScreen for example usage
 *
 * Needs to be wrapped inside a Box with fillMaxSize for expanded search bar to work properly
 * Other content might need to be offset by the height of the search bar (76.dp)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlobalSearchBar(
    userName: String,
    userAvatarModel: Any?,
    modifier: Modifier = Modifier,
    onAvatarClicked: () -> Unit = {},
    onExpandedChange: (Boolean) -> Unit = {},
    isVisible: Boolean = true,
    vm: MainScreenViewModel,
    onRestaurantSelected: (noNeedToFetchAgainBuddy: NoNeedToFetchAgainBuddy) -> Unit
) {
    if (!isVisible) return
    var query by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    // TODO: Implement search
    val onSearch: (String) -> Unit = {}

    // query search debounce
    LaunchedEffect(query) {
        vm.searchRestaurants(
            query = query
        )
    }

    LaunchedEffect(expanded) {
        onExpandedChange(expanded)
    }

    val searchBarPadding by animateDpAsState(
        targetValue = if (expanded) 0.dp else 16.dp,
        label = "Global search bar padding"
    )

    BackHandler(enabled = expanded) {
        expanded = false
    }

    SearchBar(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = searchBarPadding),
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = { query = it },
                onSearch = { vm.searchRestaurants(query = query) },
                expanded = expanded,
                onExpandedChange = {
                    expanded = it
                    // Clear query text when search bar expands
                    query = ""
                    // Hide keyboard when search bar closes
                    if (!it) keyboardController?.hide()
                },
                placeholder = { Text("Trà đào, Phúc Long") },
                leadingIcon = {
                    if (expanded) Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.clickable {
                            expanded = false // Collapse search bar
                            keyboardController?.hide() // Hide keyboard
                        }
                    )
                    else Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                    )
                },
                trailingIcon = {
                    if (expanded) {
                        if (query.isNotEmpty()) Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear",
                            modifier = Modifier.clickable {
                                query = "" // Clear query text
                            }
                        )
                    } else AsyncImageOrMonogram(
                        model = userAvatarModel,
                        name = userName,
                        contentDescription = "User avatar",
                        size = 38.dp,
                        onClick = onAvatarClicked
                    )
                },
            )
        },
        expanded = expanded,
        onExpandedChange = {
            expanded = it
            // Hide keyboard when search bar closes
            if (!it) keyboardController?.hide()
        },
    ) {
        if (expanded && query.isNotEmpty()) {
            val results by vm.searchResults.collectAsState()
            if (results.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(results) { restaurant ->
                        val noNeedToFetchAgainBuddy = NoNeedToFetchAgainBuddy(
                            Id = restaurant.id,
                            TimeAway = restaurant.estimatedTimeInMinutes,
                            Distance = restaurant.distanceInKm,
                            IsFavorited = restaurant.isFavorited
                        )
                        ListItem(
                            headlineContent = { Text(restaurant.name) },
                            supportingContent = { Text(restaurant.address) },
                            modifier = Modifier
                                .clickable {
                                    onRestaurantSelected(noNeedToFetchAgainBuddy)
                                    query = restaurant.name
                                    expanded = false
                                    keyboardController?.hide()
                                }
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No results found")
                }
            }
        }
    }
}

@Preview
@Composable
private fun SearchPrev() {
    ComponentPreview {
        Box(Modifier.fillMaxSize()) {
            GlobalSearchBar(
                userName = "Ho Nguyen",
                userAvatarModel = "",
                modifier = Modifier.align(Alignment.TopCenter),
                vm = hiltViewModel(),
                onRestaurantSelected = {}
            )
            LazyColumn(
                contentPadding = PaddingValues(
                    start = 16.dp,
                    top = 68.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                val list = List(100) { "Text $it" }
                items(count = list.size) {
                    Text(
                        text = list[it],
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    )
                }
            }
        }
    }
}