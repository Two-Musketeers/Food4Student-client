package com.ilikeincest.food4student

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ilikeincest.food4student.component.MonogramAvatar
import com.ilikeincest.food4student.component.OrderCard
import com.ilikeincest.food4student.component.OrderItemCard
import com.ilikeincest.food4student.model.OrderItem
import com.ilikeincest.food4student.ui.theme.Food4StudentTheme
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Food4StudentTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    OrderCard(
                        id = "5ea765ds",
                        date = LocalDate.of(1969, 2, 28),
                        shopName = "Phúc Long",
                        shopId = "fuck u",
                        shopImage = { MonogramAvatar(initials = "PL", it) },
                        orderItems = listOf(
                            OrderItem("Trà sữa Phô mai tươi", "Size S - không đá", 2, 54_000),
                            OrderItem("Trà sữa Phô mai tươi 2", "Size S - không đá", 2, 54_000),
                            OrderItem("Trà sữa Phô mai tươi 3", "Size S - không đá", 2, 54_000),
                        ),
                        modifier = Modifier.fillMaxWidth().padding(innerPadding)
                    )
                }
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