package com.ilikeincest.food4student.screen.checkout.confirm

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilikeincest.food4student.component.preview_helper.ScreenPreview
import com.ilikeincest.food4student.model.SavedShippingLocation
import com.ilikeincest.food4student.model.SavedShippingLocationType
import com.ilikeincest.food4student.screen.checkout.confirm.component.CheckoutShippingLocationCard
import com.ilikeincest.food4student.screen.checkout.confirm.component.PreviewOrderCard

@Composable
fun CheckoutConfirmScreen() {
    // reject if no selected shipping location
    Scaffold { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            // TODO: mock vm
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

            PreviewOrderCard(
                restaurantName = "Test",
                shopImageUrl = "what",
                orderItems = listOf(),
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )
        }
    }
}

@Preview
@Composable
private fun P() { ScreenPreview {
    CheckoutConfirmScreen()
} }