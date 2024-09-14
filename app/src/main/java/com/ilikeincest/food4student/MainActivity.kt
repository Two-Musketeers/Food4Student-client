package com.ilikeincest.food4student

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ilikeincest.food4student.ui.theme.Food4StudentTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Food4StudentTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TestLogInWithGoogle(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun TestLogInWithGoogle(
    modifier: Modifier = Modifier,
    gameViewModel: AppViewModel = viewModel()
) {
    Column(modifier = modifier) {
        AuthenticationButton(
            "Login bitch",
            onGetCredentialResponse = { credential ->
                gameViewModel.onSignInWithGoogle(credential)
            }
        )
    }
}