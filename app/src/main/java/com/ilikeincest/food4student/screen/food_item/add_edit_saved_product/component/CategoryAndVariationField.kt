package com.ilikeincest.food4student.screen.food_item.add_edit_saved_product.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun CategoryAndVariationField(
    startIcon: ImageVector,
    endIcon: ImageVector,
    title: String,
    placeholder: String,
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        shape = OutlinedTextFieldDefaults.shape,
        border = BorderStroke(
            brush = SolidColor(colorScheme.outline),
            width = 1.dp
        ),
        onClick = { onClick() },
        modifier = modifier.height(56.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    startIcon, null,
                    modifier = Modifier.padding(12.dp)
                )
                Text(
                    text = title,
                    style = typography.bodyLarge,
                    color = colorScheme.onSurface,

                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                val showPlaceholder = value.isEmpty()
                val textColor =
                    if (showPlaceholder) colorScheme.onSurfaceVariant
                    else colorScheme.onSurface
                val textToShow =
                    if (showPlaceholder) placeholder
                    else value
                Text(
                    text = textToShow,
                    style = typography.bodyLarge,
                    color = textColor,
                )
                Icon(
                    endIcon, null,
                    modifier = Modifier.padding(top = 12.dp, end = 12.dp, bottom = 12.dp, start = 4.dp)
                )
            }
        }
    }
}