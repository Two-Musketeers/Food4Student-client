package com.ilikeincest.food4student.screen.food_item.add_category.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Dialog for adding/editing a category
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDialog(
    isEditingCategory: Boolean,
    categoryName: String,
    onCategoryNameChange: (String) -> Unit,
    onSaveCategory: () -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(if (isEditingCategory) "Sửa danh mục" else "Thêm danh mục")
        },
        text = {
            OutlinedTextField(
                value = categoryName,
                onValueChange = onCategoryNameChange,
                label = { Text("Tên danh mục") },
                singleLine = true,
                trailingIcon = {
                    if (categoryName.isNotEmpty()) {
                        IconButton(onClick = { onCategoryNameChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear Text"
                            )
                        }
                    }
                },
                modifier = Modifier.Companion.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(
                onClick = onSaveCategory,
                enabled = categoryName.isNotBlank()
            ) {
                Text("Lưu")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Hủy")
            }
        },
        modifier = Modifier.Companion.padding(16.dp)
    )
}