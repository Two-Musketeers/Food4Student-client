package com.ilikeincest.food4student

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ilikeincest.food4student.platform.initializer.initializeHERESDK
import com.ilikeincest.food4student.service.ApiService
import com.ilikeincest.food4student.ui.theme.Food4StudentTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var apiService: ApiService

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
                val coroutineScope = rememberCoroutineScope()
                Button(onClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        val a = apiService.getLmao()
                        a.raw().body
                    }
                }, modifier = Modifier.padding(top = 32.dp)) {
                    Text("lmao")
                }
            }
        }
    }
}