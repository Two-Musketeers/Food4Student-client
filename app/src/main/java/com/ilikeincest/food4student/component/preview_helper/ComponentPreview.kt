package com.ilikeincest.food4student.component.preview_helper

import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.ilikeincest.food4student.ui.theme.Food4StudentTheme

@Composable
fun ComponentPreview(composable: @Composable () -> Unit) {
    Food4StudentTheme {
        Surface(color = colorScheme.background) {
            composable()
        }
    }
}