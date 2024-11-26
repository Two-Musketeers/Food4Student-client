package com.ilikeincest.food4student.screen.main_page.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.ilikeincest.food4student.component.preview_helper.ScreenPreview
import com.ilikeincest.food4student.screen.main_page.home.component.CurrentShippingLocationCard

@Composable
fun HomeScreen(
    onNavigateToShippingLocation: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp).padding(top = 12.dp)
    ) {
        item {
            CurrentShippingLocationCard(
                onClick = onNavigateToShippingLocation,
                currentLocation = "24 Lý Thường Kiệt, Quận 69, Tp. Thủ Đức"
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun Prev() { ScreenPreview {
    HomeScreen({})
} }