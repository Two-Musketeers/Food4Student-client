package com.ilikeincest.food4student.admin.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
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
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import coil3.compose.rememberAsyncImagePainter
import com.ilikeincest.food4student.admin.viewmodel.AdminRestaurantViewModel
import com.ilikeincest.food4student.model.Restaurant

@Composable
fun RestaurantItem(
    restaurant: Restaurant,
    viewModel: AdminRestaurantViewModel
) {
    var showDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDialog = true }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Display the logo if available
        if (restaurant.logoUrl != null) {
            Image(
                painter = rememberAsyncImagePainter(model = restaurant.logoUrl),
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
        } else {
            Icon(
                imageVector = Icons.Default.Restaurant,
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = "Id: ${restaurant.id}",
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = "Name: ${restaurant.name}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Ratings: ${restaurant.averageRating} (${restaurant.totalRatings} reviews)",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Approved: ${if (restaurant.isApproved) "Yes" else "No"}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Address: ${restaurant.address}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }

    if (showDialog) {
        RestaurantActionDialog(
            restaurant = restaurant,
            onDismiss = { showDialog = false },
            viewModel = viewModel
        )
    }
}