package com.ilikeincest.food4student.screen.checkout.confirm.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ilikeincest.food4student.model.SavedShippingLocation
import com.ilikeincest.food4student.model.SavedShippingLocationType

@Composable
fun CheckoutShippingLocationCard(
    onClick: () -> Unit,
    currentLocation: SavedShippingLocation,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.LocationOn, null)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(currentLocation.name.orEmpty(),
                            style = typography.titleMedium
                        )
                        Text(
                            currentLocation.phoneNumber.toString(),
                            style = typography.bodyLarge,
                            color = colorScheme.onSurfaceVariant,
                        )
                    }
                }
                HorizontalDivider(Modifier.width(240.dp).padding(vertical = 8.dp))
                if (currentLocation.address != currentLocation.location) {
                    Text(currentLocation.location, style = typography.bodyMedium.copy(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.W600
                    ))
                }
                Text(currentLocation.address)
                if (!currentLocation.buildingNote.isNullOrBlank()) {
                    Spacer(Modifier.height(12.dp))
                    Text("Ghi chú: ${currentLocation.buildingNote}",
                        color = colorScheme.onSurfaceVariant
                    )
                }
            }
            Icon(Icons.Default.ChevronRight, null)
        }
    }
}

@Preview
@Composable
private fun P() {
    CheckoutShippingLocationCard({}, SavedShippingLocation(
        "",
        locationType = SavedShippingLocationType.Work,
        buildingNote = "Cổng trước",
        location = "KTX Đại học Quốc gia TPHCM - Khu B",
        address = "15 Tô Vĩnh Diện, Phường Đông Hòa, Dĩ An, Bình Dương",
        name = "Hồ Nguyên Minh",
        phoneNumber = "01234567879",
        latitude = 0.0, longitude = 0.0,
    ), Modifier.padding(16.dp))
}