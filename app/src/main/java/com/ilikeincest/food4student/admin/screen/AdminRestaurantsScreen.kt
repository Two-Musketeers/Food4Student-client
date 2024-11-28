package com.ilikeincest.food4student.admin.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ilikeincest.food4student.admin.component.AdminRestaurantItem
import com.ilikeincest.food4student.admin.viewmodel.AdminRestaurantViewModel
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import com.ilikeincest.food4student.admin.component.LoadingItem
import com.ilikeincest.food4student.admin.component.NoMoreRestaurant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminRestaurantsScreen(
    viewModel: AdminRestaurantViewModel = hiltViewModel()
) {
    val restaurants = viewModel.restaurants
    val searchQuery = viewModel.searchQuery
    val isFilterApproved = viewModel.isFilterApproved
    val isLoading by remember { derivedStateOf { viewModel.isLoading } }
    val hasMore by remember { derivedStateOf { viewModel.hasMore } }

    // Scroll state to detect when to load more
    val listState = rememberLazyListState()

    // Detect when scrolled to the 5th item from the end to load more
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                if (visibleItems.isNotEmpty()) {
                    val lastVisibleItemIndex = visibleItems.last().index
                    if (lastVisibleItemIndex >= viewModel.restaurants.size - 5 && !isLoading && hasMore) {
                        viewModel.loadMoreRestaurants()
                    }
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Search Bar with Filter Icon
        TextField(
            value = searchQuery,
            onValueChange = { newQuery ->
                viewModel.updateSearchQuery(newQuery)
            },
            label = { Text("Search by ID or Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon"
                )
            },
            trailingIcon = {
                IconButton(onClick = { viewModel.toggleFilterApproved() }) {
                    Icon(
                        imageVector = if (isFilterApproved) Icons.Default.FilterList else Icons.Default.FilterAlt,
                        contentDescription = "Filter Icon"
                    )
                }
            },
        )

        // Restaurant List with Pagination
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(restaurants) { restaurant ->
                AdminRestaurantItem(restaurant = restaurant, viewModel = viewModel)
            }

            // Loading Indicator
            if (isLoading) {
                item {
                    LoadingItem()
                }
            }

            // No More Data Indicator
            if (!hasMore && !isLoading) {
                item {
                    NoMoreRestaurant()
                }
            }
        }
    }
}