package com.ilikeincest.food4student.screen.shipping.shipping_location.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.ilikeincest.food4student.R
import com.ilikeincest.food4student.component.preview_helper.ComponentPreview
import com.ilikeincest.food4student.model.SavedShippingLocationType as LocationType

@Composable
fun AddLocationCard(
    onClick: () -> Unit,
    locationType: LocationType,
    modifier: Modifier = Modifier
) {
    val (icon, label) = when (locationType) {
        LocationType.Home -> Pair(R.drawable.add_home, "nhà")
        LocationType.Work -> Pair(R.drawable.domain_add, "cơ quan")
        LocationType.Other -> Pair(R.drawable.bookmark_add, "khác")
    }

    OutlinedCard(
        onClick = onClick,
        colors = CardDefaults.outlinedCardColors()
            .copy(containerColor = colorScheme.surfaceContainerLow),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Box(Modifier
                .background(colorScheme.primaryContainer, CircleShape)
                .size(40.dp)
            ) {
                Icon(
                    painterResource(icon),
                    contentDescription = null,
                    tint = colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.Center)
                )
            }
            Text("Thêm địa chỉ $label",
                style = typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            Icon(Icons.Default.ChevronRight, null)
        }
    }
}

// tf do u mean "Redeclaration: LocationTypePreview"???
// its fucking private you fucking twat
// remove the "2" when kotlin fucking fixes itself
private class LocationTypePreview2 : CollectionPreviewParameterProvider<LocationType>(listOf(
    LocationType.Home, LocationType.Work, LocationType.Other
))

@PreviewLightDark
@Composable
private fun Prev(@PreviewParameter(LocationTypePreview2::class) locationType: LocationType) { ComponentPreview {
    AddLocationCard(
        locationType = locationType,
        onClick = {},
        modifier = Modifier
            .width(385.dp)
            .padding(16.dp)
    )
} }