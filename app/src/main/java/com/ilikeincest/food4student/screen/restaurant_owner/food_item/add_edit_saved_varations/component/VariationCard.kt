package com.ilikeincest.food4student.screen.restaurant_owner.food_item.add_edit_saved_varations.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ilikeincest.food4student.R
import com.ilikeincest.food4student.model.Variation

@Composable
fun VariationCard(
    variation: Variation,
    onEditVariation: () -> Unit,
    onDeleteVariation: () -> Unit,
    onAddOption: () -> Unit,
    onEditOption: (Int) -> Unit,
    onDeleteOption: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp, start = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = variation.name,
                        style = typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                    )
                    IconButton(onClick = onEditVariation) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(id = R.drawable.edit),
                            contentDescription = "Chỉnh sửa"
                        )
                    }
                }
                IconButton(onClick = onDeleteVariation) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Xoá"
                    )
                }
            }
            // Variation options
            variation.variationOptions.forEachIndexed { index, option ->
                VariationOptionCard(
                    option = option,
                    onEditOption = { onEditOption(index) },
                    onDeleteOption = { onDeleteOption(index) }
                )
            }
            // "Thêm giá trị phân loại" button
            ElevatedCard(
                onClick = onAddOption,
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = colorScheme.secondary,
                    contentColor = colorScheme.onSecondary
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                    Text(
                        text = "Thêm giá trị phân loại",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}