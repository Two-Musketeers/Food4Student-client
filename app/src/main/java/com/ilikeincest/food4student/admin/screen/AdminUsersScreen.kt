package com.ilikeincest.food4student.admin.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ilikeincest.food4student.admin.component.AdminUserItem
import com.ilikeincest.food4student.admin.component.AdminUserSearchBar
import com.ilikeincest.food4student.admin.component.LoadingItem
import com.ilikeincest.food4student.admin.component.NoMoreUser
import com.ilikeincest.food4student.admin.viewmodel.AdminUserViewModel

@Composable
fun AdminUsersScreen(viewModel: AdminUserViewModel = hiltViewModel()) {
    val users = viewModel.users
    val searchQuery = viewModel.searchQuery
    val isLoading = viewModel.isLoading
    val hasMore = viewModel.hasMore

    // Scroll state to detect when to load more
    val listState = rememberLazyListState()

    // Detect when scrolled to the 10th item from the end to load more
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                if (visibleItems.isNotEmpty()) {
                    val lastVisibleItemIndex = visibleItems.last().index
                    if (lastVisibleItemIndex >= users.size - 10 && !isLoading && hasMore) {
                        viewModel.loadMoreUsers()
                    }
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Search Bar with Filter Icon
        AdminUserSearchBar(
            searchQuery = searchQuery,
            onSearchQueryChange = { newQuery ->
                viewModel.updateSearchQuery(newQuery)
            },
            onRoleFilterChange = { role ->
                viewModel.updateSelectedRole(role)
            }
        )

        // User List with Pagination
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(users) { user ->
                AdminUserItem(user = user, viewModel = viewModel)
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
                    NoMoreUser()
                }
            }
        }
    }
}

