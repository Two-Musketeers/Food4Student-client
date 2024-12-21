package com.ilikeincest.food4student.screen.food_item.add_category.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ilikeincest.food4student.model.FoodCategory

@Composable
fun CategoryItem(
    category: FoodCategory,
    onEditCategory: () -> Unit,
    onDeleteCategory: () -> Unit,
    onClick: () -> Unit,
    isSelected: Boolean
) {
    Card(
        colors = if (isSelected) CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(
                alpha = 0.4f
            )
        )
        else CardDefaults.cardColors(),
        modifier = Modifier.Companion
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.Companion.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = category.name,
                style = MaterialTheme.typography.bodyLarge
            )
            Row {
                IconButton(onClick = onEditCategory) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Chỉnh sửa"
                    )
                }
                IconButton(onClick = onDeleteCategory) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Xóa"
                    )
                }
            }
        }
    }
}