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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.ilikeincest.food4student.R
import com.ilikeincest.food4student.component.preview_helper.ComponentPreview

@Composable
fun CurrentLocationCard(
    currentLocation: String,
    locationAddress: String,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        colors = CardDefaults.outlinedCardColors()
            .copy(containerColor = colorScheme.surfaceContainerLow),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(Modifier
            .padding(16.dp)
            .fillMaxWidth()
        ) {
            Box(
                Modifier
                    .background(colorScheme.primaryContainer, CircleShape)
                    .size(40.dp)
            ) {
                Icon(
                    painterResource(R.drawable.location_on),
                    contentDescription = null,
                    tint = colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.Center)
                )
            }
            Spacer(Modifier.width(16.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Địa chỉ giao hàng hiện tại:", style = typography.titleSmall)
                Text(currentLocation, style = typography.titleMedium)
                Text(locationAddress, style = typography.bodySmall)
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun Prev() { ComponentPreview {
    CurrentLocationCard(
        "KTX Đại học Quốc gia TPHCM - Khu B",
        "15/12/564/23 Tô Vĩnh Diện, Phường Đông Hòa, Dĩ An, Bình Dương",
        Modifier
            .width(385.dp)
            .padding(16.dp)
    )
} }