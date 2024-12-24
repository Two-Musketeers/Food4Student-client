package com.ilikeincest.food4student.screen.shipping.add_edit_saved_location.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
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
import androidx.compose.ui.unit.dp

@Composable
fun AddressField(
    text: String,
    placeholder: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        shape = OutlinedTextFieldDefaults.shape,
        border = BorderStroke(
            brush = SolidColor(colorScheme.outline),
            width = 1.dp
        ),
        onClick = onClick,
        modifier = modifier.defaultMinSize(minHeight = 56.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.defaultMinSize(minHeight = 56.dp)
        ) {
            val showPlaceholder = text.isEmpty()
            val textColor =
                if (showPlaceholder) colorScheme.onSurfaceVariant
                else colorScheme.onSurface
            val textToShow =
                if (showPlaceholder) placeholder
                else text
            Text(textToShow,
                style = typography.bodyLarge,
                color = textColor,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .padding(vertical = 16.dp)
                    .weight(1f)
            )
            Icon(
                Icons.Default.ChevronRight, null,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}