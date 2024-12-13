package com.ilikeincest.food4student.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ilikeincest.food4student.component.preview_helper.ScreenPreview

@Composable
fun ErrorDialog(
    message: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = { Text("Lỗi kìa bạn.") },
        text = { Text(message) },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("OK")
            }
        },
        onDismissRequest = onDismiss,
        modifier = modifier,
        icon = { Icon(Icons.Default.Error, null) },
    )
}

@Preview
@Composable
private fun Prev() { ScreenPreview {
    ErrorDialog("holy shiet", {})
} }