package com.ilikeincest.food4student.screen.restaurant_owner.food_item

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ConfirmDeleteDialog(
    title: String,
    text: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = { Text(title) },
        text = { Text(text) },
        confirmButton = {
            Button(onClick = onConfirm, colors = ButtonDefaults.buttonColors().copy(
                containerColor = colorScheme.error
            )) {
                Text("Xoá",
                    color = colorScheme.onError
                )
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) {
            Text("Huỷ")
        } },
        onDismissRequest = onDismiss,
        modifier = modifier,
        icon = { Icon(Icons.Default.Warning, null) },
    )
}