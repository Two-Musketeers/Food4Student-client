package com.ilikeincest.food4student.admin.component

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.ilikeincest.food4student.admin.viewmodel.AdminUserViewModel
import com.ilikeincest.food4student.model.User

@Composable
fun AdminUserActionDialog(
    user: User,
    onDismiss: () -> Unit,
    viewModel: AdminUserViewModel
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("User Actions") },
        text = { Text("Select an action for ${user.displayName ?: "this user"}") },
        confirmButton = {
            Column {
                // Ban or Unban User
                val isBanned = user.role == "Banned"
                TextButton(onClick = {
                    viewModel.updateUserRole(user.id, if (isBanned) "User" else "Banned")
                    onDismiss()
                    Toast.makeText(
                        context,
                        if (isBanned) "User Unbanned" else "User Banned",
                        Toast.LENGTH_SHORT
                    ).show()
                }) {
                    Text(if (isBanned) "Unban User" else "Ban User")
                }

                // Grant or Revoke Moderator Role
                when (user.role) {
                    "User" -> {
                        TextButton(onClick = {
                            viewModel.updateUserRole(user.id, "Moderator")
                            onDismiss()
                            Toast.makeText(context, "Granted Moderator Role", Toast.LENGTH_SHORT).show()
                        }) {
                            Text("Grant Moderator Role")
                        }
                    }
                    "Moderator" -> {
                        TextButton(onClick = {
                            viewModel.updateUserRole(user.id, "User")
                            onDismiss()
                            Toast.makeText(context, "Revoked Moderator Role", Toast.LENGTH_SHORT).show()
                        }) {
                            Text("Revoke Moderator Role")
                        }
                    }
                    else -> { /* Do nothing for other roles */ }
                }

                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        },
        dismissButton = {}
    )
}