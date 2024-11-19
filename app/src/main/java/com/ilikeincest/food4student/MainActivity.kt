package com.ilikeincest.food4student

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ilikeincest.food4student.platform.initializer.initializeHERESDK
import com.ilikeincest.food4student.ui.theme.Food4StudentTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeHERESDK(applicationContext)
        // shopee food have none of them landscape too, fuck em ei?
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        enableEdgeToEdge()
        setContent {
            Food4StudentTheme {
                AppNavGraph()
            }
        }
    }
}