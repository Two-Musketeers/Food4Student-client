package com.ilikeincest.food4student

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.firebase.messaging.FirebaseMessaging
import com.ilikeincest.food4student.platform.initializer.initializeHERESDK
import com.ilikeincest.food4student.screen.food_item.add_category.AddCategoryScreen
import com.ilikeincest.food4student.ui.theme.Food4StudentTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeHERESDK(applicationContext)
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            Log.d("FCM token", it)
        }
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