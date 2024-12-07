package com.ilikeincest.food4student.screen.main_page.home.component

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.ilikeincest.food4student.component.LoopingHorizontalPager

@Preview
@Composable
fun AdsBanner(modifier: Modifier = Modifier) {
    LoopingHorizontalPager(
        itemCount = 5,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .aspectRatio(3f),
    ) {
        AsyncImage(
            "https://unsplash.com/photos/IaPlDU14Oig/download?ixid=M3wxMjA3fDB8MXxhbGx8OXx8fHx8fDJ8fDE3MjY1NTQ2MDN8&force=true&w=640",
            contentDescription = "Ad banner",
            placeholder = ColorPainter(colorScheme.primaryContainer),
            error = ColorPainter(colorScheme.error),
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop, // shouldnt be needed but eh
        )
    }
}
