package com.ilikeincest.food4student

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ilikeincest.food4student.platform.initializer.initializeHERESDK
import com.ilikeincest.food4student.ui.theme.Food4StudentTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeHERESDK(applicationContext)

        enableEdgeToEdge()
        setContent {
            Food4StudentTheme {
                AppNavGraph()
            }
        }
    }
}