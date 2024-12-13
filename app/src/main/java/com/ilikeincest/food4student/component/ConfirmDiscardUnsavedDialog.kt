package com.ilikeincest.food4student.component

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
import androidx.compose.ui.tooling.preview.Preview
import com.ilikeincest.food4student.component.preview_helper.ScreenPreview

@Composable
fun ConfirmDiscardUnsavedDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = { Text("Chưa lưu thay đổi") },
        text = { Text("Bạn có thay đổi chưa lưu. Tiếp tục chỉnh sửa hay hủy sửa đổi?") },
        confirmButton = {
            Button(onClick = onConfirm, colors = ButtonDefaults.buttonColors().copy(
                containerColor = colorScheme.error
            )) {
                Text("Hủy sửa đổi",
                    color = colorScheme.onError
                )
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) {
            Text("Tiếp tục sửa")
        } },
        onDismissRequest = onDismiss,
        modifier = modifier,
        icon = { Icon(Icons.Default.Warning, null) },
    )
}

@Preview
@Composable
private fun Prev() { ScreenPreview {
    ConfirmDiscardUnsavedDialog({}, {})
} }