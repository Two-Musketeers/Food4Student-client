package com.ilikeincest.food4student.screen.food_item.add_edit_saved_varations.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ilikeincest.food4student.R
import com.ilikeincest.food4student.model.VariationOption

@Composable
fun VariationOptionCard(
    option: VariationOption,
    onEditOption: () -> Unit,
    onDeleteOption: () -> Unit
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${option.name} - ${option.priceAdjustment}đ",
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    modifier = Modifier
                        .size(16.dp)
                        .clickable(onClick = onEditOption),
                    painter = painterResource(id = R.drawable.edit),
                    contentDescription = "Chỉnh sửa"
                )
                Icon(
                    modifier = Modifier
                        .clickable(onClick = onDeleteOption)
                        .padding(start = 8.dp),
                    imageVector = Icons.Default.Close,
                    contentDescription = "Xoá"
                )
            }
        }
    }
}