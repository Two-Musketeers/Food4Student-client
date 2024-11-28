package com.ilikeincest.food4student.admin.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.FilterList

@Composable
fun AdminUserSearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onRoleFilterChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    TextField(
        value = searchQuery,
        onValueChange = { newQuery ->
            onSearchQueryChange(newQuery)
        },
        label = { Text("Search by ID, Name, or Email") },
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
            IconButton(onClick = { expanded = true }) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filter Icon"
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                val roles = listOf("All", "Banned", "Moderator", "Admin", "RestaurantOwner")
                roles.forEach { role ->
                    DropdownMenuItem(
                        text = { Text(role) },
                        onClick = {
                            onRoleFilterChange(role)
                            expanded = false
                        }
                    )
                }
            }
        }
    )
}