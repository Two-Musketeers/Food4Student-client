package com.ilikeincest.food4student.admin.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun AdminActionListItem(text: String, icon: ImageVector, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(text) },
        leadingContent = { Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
        modifier = Modifier.Companion
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(Color.Transparent)
    )
}