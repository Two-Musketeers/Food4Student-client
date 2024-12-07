package com.ilikeincest.food4student.screen.main_page.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.ilikeincest.food4student.component.preview_helper.ScreenPreview
import com.ilikeincest.food4student.screen.main_page.home.component.AdsBanner
import com.ilikeincest.food4student.screen.main_page.home.component.CurrentShippingLocationCard

@Composable
fun HomeScreen(
    onNavigateToShippingLocation: () -> Unit
) {
    Column(Modifier
        .padding(horizontal = 16.dp)
        .padding(top = 12.dp)
    ) {
        CurrentShippingLocationCard(
            onClick = onNavigateToShippingLocation,
            currentLocation = "24 Lý Thường Kiệt, Quận 69, Tp. Thủ Đức",
            modifier = Modifier.padding(bottom = 12.dp)
        )
        LazyColumn {
            item {
                AdsBanner()
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun Prev() { ScreenPreview {
    HomeScreen({})
} }