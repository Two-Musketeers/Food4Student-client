package com.ilikeincest.food4student.admin.component

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.ilikeincest.food4student.admin.viewmodel.AdminRestaurantViewModel
import com.ilikeincest.food4student.model.Restaurant

@Composable
fun RestaurantActionDialog(
    restaurant: Restaurant,
    onDismiss: () -> Unit,
    viewModel: AdminRestaurantViewModel
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Restaurant Actions") },
        text = { Text("Select an action for ${restaurant.name ?: "this restaurant"}") },
        confirmButton = {
            Column {
                // Approve or Unapprove Restaurant
                val isApproved = restaurant.isApproved
                TextButton(onClick = {
                    viewModel.updateRestaurantApproval(restaurant.id ?: "", !isApproved)
                    onDismiss()
                    Toast.makeText(
                        context,
                        if (isApproved) "Restaurant Unapproved" else "Restaurant Approved",
                        Toast.LENGTH_SHORT
                    ).show()
                }) {
                    Text(if (isApproved) "Unapprove Restaurant" else "Approve Restaurant")
                }

                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        },
        dismissButton = {}
    )
}