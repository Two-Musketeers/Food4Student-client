package com.ilikeincest.food4student.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle

@Composable
fun MonogramAvatar(
    initials: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = typography.bodySmall,
    onClick: () -> Unit = {}
) {
    val bgColor = colorScheme.primaryContainer
    Box(
        modifier
            .clip(CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(SolidColor(bgColor))
        }
        Text(text = initials, style = textStyle, color = colorScheme.onPrimaryContainer)
    }
}