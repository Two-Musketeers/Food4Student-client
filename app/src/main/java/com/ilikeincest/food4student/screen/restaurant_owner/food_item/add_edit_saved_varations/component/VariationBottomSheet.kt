package com.ilikeincest.food4student.screen.restaurant_owner.food_item.add_edit_saved_varations.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VariationBottomSheet(
    isEditingVariation: Boolean,
    variationName: String,
    minSelect: String,
    maxSelect: String,
    onVariationNameChange: (String) -> Unit,
    onMinChange: (String) -> Unit,
    onMaxChange: (String) -> Unit,
    onSaveVariation: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = if (isEditingVariation) "Sửa phân loại hàng" else "Thêm phân loại hàng",
                style = typography.titleLarge,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = variationName,
                onValueChange = onVariationNameChange,
                label = { Text(text = "Nhập tên phân loại") },
                singleLine = true,
                trailingIcon = {
                    if (variationName.isNotEmpty()) {
                        IconButton(onClick = { onVariationNameChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear Text"
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = minSelect,
                onValueChange = onMinChange,
                label = { Text(text = "Số chọn tối thiểu") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                trailingIcon = {
                    if (minSelect.isNotEmpty()) {
                        IconButton(onClick = { onMinChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear Text"
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = when {
                    isEditingVariation && maxSelect == "null" -> "Không có giới hạn"
                    else -> maxSelect
                },
                onValueChange = onMaxChange,
                label = { Text(text = "Số chọn tối đa (không bắt buộc)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                supportingText = { Text("Để trống nếu không có giới hạn về chọn tối đa") },
                trailingIcon = {
                    if (maxSelect.isNotEmpty()) {
                        IconButton(onClick = { onMaxChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear Text"
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onSaveVariation,
                modifier = Modifier.align(Alignment.End),
                enabled = variationName.isNotBlank()
            ) {
                Text("Lưu")
            }
        }
    }
}