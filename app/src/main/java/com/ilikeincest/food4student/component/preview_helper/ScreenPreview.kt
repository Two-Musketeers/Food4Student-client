package com.ilikeincest.food4student.component.preview_helper

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.ilikeincest.food4student.ui.theme.Food4StudentTheme

/**
 * Helper composable used for previewing UI screens.
 *
 * Already wrapped inside app theme and scaffold for
 * dark/light theme support.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScreenPreview(composable: @Composable () -> Unit) {
    Food4StudentTheme {
        Scaffold {
            composable()
        }
    }
}