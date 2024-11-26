package com.ilikeincest.food4student.screen.main_page.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilikeincest.food4student.component.preview_helper.ComponentPreview

@Composable
fun CurrentShippingLocationCard(
    onClick: () -> Unit,
    currentLocation: String,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
//        colors = CardDefaults.cardColors()
//            .copy(containerColor = colorScheme.surfaceContainerHigh),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(Icons.Rounded.LocationOn, null)
            Text("Giao đến:", style = typography.titleMedium)
            Text(
                currentLocation,
                style = typography.bodyLarge,
                color = colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Icon(Icons.Default.ChevronRight, null)
        }
    }
}

@Preview
@Composable
private fun Prev() { ComponentPreview {
    CurrentShippingLocationCard(
        onClick = {},
        currentLocation = "24 Lý Thường Kiệt, Quận 69, Tp. Thủ Đức",
        modifier = Modifier.width(400.dp).padding(16.dp)
    )
} }