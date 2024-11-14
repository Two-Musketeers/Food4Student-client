package com.ilikeincest.food4student.component.preview_helper

import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.toArgb
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import coil3.test.FakeImage
import com.ilikeincest.food4student.ui.theme.Food4StudentTheme

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ComponentPreview(composable: @Composable () -> Unit) {
    val previewColor = colorScheme.primary
    val previewHandler = AsyncImagePreviewHandler {
        FakeImage(color = previewColor.toArgb())
    }
    Food4StudentTheme {
        Surface(color = colorScheme.background) {
            CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
                composable()
            }
        }
    }
}