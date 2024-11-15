package com.ilikeincest.food4student.component.badge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
@Preview
fun DotBadge(
    modifier: Modifier = Modifier,
    size: Dp = 10.dp
) {
    Spacer(modifier
        .clip(CircleShape)
        .size(size)
        .background(colorScheme.error)
    )
}