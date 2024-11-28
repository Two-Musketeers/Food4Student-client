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
import com.ilikeincest.food4student.admin.component.RestaurantItem
import com.ilikeincest.food4student.admin.viewmodel.AdminRestaurantViewModel
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.items

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminRestaurantsScreen(
    viewModel: AdminRestaurantViewModel = hiltViewModel()
) {
    val restaurants by viewModel.restaurants
    val searchQuery by viewModel.searchQuery
    val isFilterApproved by viewModel.isFilterApproved

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
            }
        )

        // Restaurant List
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(restaurants) { restaurant ->
                RestaurantItem(restaurant = restaurant, viewModel = viewModel)
            }
        }
    }
}