package com.ilikeincest.food4student.screen.shipping_location.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
fun SavedLocationCard(
    locationType: LocationType,
    location: String,
    address: String,
    receiverName: String,
    receiverPhone: String,
    onSelected: () -> Unit,
    onEditLocation: () -> Unit,
    modifier: Modifier = Modifier,
    buildingNote: String? = null,
    otherLocationTypeTitle: String? = null,
) {
    val otherTypeTitle = otherLocationTypeTitle ?: "Khác"
    val (locationTypeTitle, icon) = when (locationType) {
        LocationType.Home -> Pair("Nhà", R.drawable.home)
        LocationType.Work -> Pair("Cơ quan", R.drawable.domain)
        LocationType.Other -> Pair(otherTypeTitle, R.drawable.bookmark)
    }
    val note = if (buildingNote == null) "" else "[$buildingNote] "
    val locationTitle = "$note$location"
    val contactInfo = "$receiverName, $receiverPhone"

    OutlinedCard(
        onClick = onSelected,
        colors = CardDefaults.outlinedCardColors()
            .copy(containerColor = colorScheme.surfaceContainerLow),
        modifier = modifier.fillMaxWidth()
    ) { Box(Modifier.fillMaxWidth()) {
        Row(Modifier
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
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Spacer(Modifier.width(16.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    locationTypeTitle,
                    style = typography.titleMedium,
                    modifier = Modifier.fillMaxWidth().padding(end = 52.dp)
                )
                Column {
                    Text(locationTitle, style = typography.bodyMedium)
                    Text(
                        address,
                        style = typography.bodyMedium,
                        color = colorScheme.onSurfaceVariant
                    )
                }
                Text(contactInfo, style = typography.bodySmall)
            }
        }
        TextButton(
            onClick = onEditLocation,
            content = { Text("Sửa") },
            modifier = Modifier.align(Alignment.TopEnd).padding(top = 4.dp, end = 10.dp)
        )
    } }
}

private class LocationTypePreview : CollectionPreviewParameterProvider<LocationType>(listOf(
    LocationType.Home, LocationType.Work, LocationType.Other
))

@PreviewLightDark
@Composable
private fun Prev(@PreviewParameter(LocationTypePreview::class) locationType: LocationType) { ComponentPreview {
    SavedLocationCard(
        locationType = locationType,
        buildingNote = "Cổng trước", // this can be null
        location = "KTX Đại học Quốc gia TPHCM - Khu B",
        address = "15 Tô Vĩnh Diện, Phường Đông Hòa, Dĩ An, Bình Dương",
        receiverName = "Hồ Nguyên Minh",
        receiverPhone = "01234567879",
        // test too long custom title, wont overlap with edit button
        otherLocationTypeTitle = "Lmao Advenas messis, tanqul castus xiphias.",
        onSelected = {},
        onEditLocation = {},
        modifier = Modifier.padding(16.dp)
    )
} }