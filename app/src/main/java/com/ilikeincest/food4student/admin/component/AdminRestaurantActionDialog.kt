package com.ilikeincest.food4student.admin.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ilikeincest.food4student.admin.viewmodel.AdminRestaurantViewModel
import com.ilikeincest.food4student.model.Restaurant

@Composable
fun AdminRestaurantActionDialog(
    restaurant: Restaurant,
    onDismiss: () -> Unit,
    viewModel: AdminRestaurantViewModel
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        dismissButton = {},
        title = {
            Text(text = "Restaurant Actions", style = MaterialTheme.typography.titleLarge)
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Select an action for ${restaurant.name}.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                    thickness = 1.dp
                )

                // Restaurant Management Actions
                Text(
                    text = "Restaurant Management",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                ) {
                    item {
                        AdminActionListItem(
                            text = if (restaurant.isApproved) "Unapprove Restaurant" else "Approve Restaurant",
                            icon = if (restaurant.isApproved) Icons.Default.Block else Icons.Default.Check,
                            onClick = {
                                viewModel.updateRestaurantApproval(restaurant.id, !restaurant.isApproved)
                                onDismiss()
                            }
                        )
                    }

                    // Add more actions here if needed
                }

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                    thickness = 1.dp
                )
            }
        },
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.95f), shape = MaterialTheme.shapes.medium)
    )
}