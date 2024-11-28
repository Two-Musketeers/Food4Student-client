package com.ilikeincest.food4student.admin.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddModerator
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import com.ilikeincest.food4student.model.User

@Composable
fun AdminUserItem(user: User) {
    var showDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .clickable { showDialog = true }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = getRoleIcon(user.role),
            contentDescription = null,
            modifier = Modifier.Companion.size(48.dp)
        )
        Spacer(modifier = Modifier.Companion.width(16.dp))
        Column {
            Text(text = "Id: ${user.id}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Name: ${user.displayName}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Email: ${user.email}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Role: ${user.role}", style = MaterialTheme.typography.bodyMedium)
        }
    }

    if (showDialog) {
        AdminUserActionDialog(
            user = user,
            onDismiss = { showDialog = false },
            viewModel = hiltViewModel()
        )
    }
}

fun getRoleIcon(role: String): ImageVector {
    return when (role) {
        "Admin" -> Icons.Filled.Android
        "User" -> Icons.Filled.Person
        "RestaurantOwner" -> Icons.Filled.Restaurant
        "Moderator" -> Icons.Filled.AddModerator
        "Banned" -> Icons.Filled.Block
        else -> Icons.Filled.Error
    }
}