package com.ilikeincest.food4student.admin.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ilikeincest.food4student.admin.component.AdminUserItem
import com.ilikeincest.food4student.admin.component.AdminUserSearchBar
import com.ilikeincest.food4student.admin.viewmodel.AdminUserViewModel

@Composable
fun AdminUsersScreen(viewModel: AdminUserViewModel = hiltViewModel()) {
    val searchQuery = viewModel.searchQuery
    val selectedRole = viewModel.selectedRole
    val users = viewModel.users

    Column(modifier = Modifier.fillMaxSize()) {
        AdminUserSearchBar(
            searchQuery = searchQuery,
            onSearchQueryChange = { newQuery ->
                viewModel.updateSearchQuery(newQuery)
            },
            selectedRole = selectedRole,
            onRoleFilterChange = { role ->
                viewModel.updateSelectedRole(role)
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