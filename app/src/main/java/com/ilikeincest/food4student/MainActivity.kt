package com.ilikeincest.food4student

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ilikeincest.food4student.screen.auth.component.AuthenticationButton
import com.ilikeincest.food4student.initializer.HereInitializer
import com.ilikeincest.food4student.screen.map.MapScreen
import com.ilikeincest.food4student.ui.theme.Food4StudentTheme
import com.ilikeincest.food4student.viewmodel.MapViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize HERE SDK
        val hereInitializer = HereInitializer(this)
        hereInitializer.initializeHERESDK()

        enableEdgeToEdge()
        setContent {
            Food4StudentTheme {
                AppNavGraph()
            }
        }
    }
}

@Composable
fun TestLogInWithGoogle(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel = viewModel()
) {
    Column(modifier = modifier) {
        AuthenticationButton(
            "Login bitch",
            onGetCredentialResponse = { credential ->
                appViewModel.onSignInWithGoogle(credential)
            }
        )
    }
}