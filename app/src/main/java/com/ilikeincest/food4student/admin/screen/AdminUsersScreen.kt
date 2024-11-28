package com.ilikeincest.food4student.admin.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ilikeincest.food4student.admin.component.AdminUserItem
import com.ilikeincest.food4student.admin.viewmodel.AdminUserViewModel

@Composable
fun AdminUsersScreen(viewModel: AdminUserViewModel = hiltViewModel()) {
    val users = viewModel.users
    val searchQuery = viewModel.searchQuery

    Column {
        // Search Bar
        TextField(
            value = searchQuery,
            onValueChange = { newQuery -> viewModel.updateSearchQuery(newQuery) },
            label = { Text("Search by ID, Name, or Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            singleLine = true,
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
            }
        )

        // User List
        LazyColumn {
            items(users) { user ->
                AdminUserItem(user = user, viewModel = viewModel)
            }
        }
    }
}