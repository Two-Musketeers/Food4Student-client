package com.ilikeincest.food4student.admin.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoadingItem() {
    Box(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Companion.Center
    ) {
        CircularProgressIndicator()
    }
}