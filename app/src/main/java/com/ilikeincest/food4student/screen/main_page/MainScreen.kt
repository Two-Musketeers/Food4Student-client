package com.ilikeincest.food4student.screen.main_page

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddHome
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = { /* Handle navigation */ },
                    label = { Text("Home") },
                    icon = { Icon(Icons.Default.AddHome, null) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("other") },
                    label = { Text("Other") },
                    icon = { /* Add icon here */ }
                )
            }
        }
    ) { innerPadding ->
        // Main screen content
        Text("Main Screen", modifier = Modifier.padding(innerPadding))
    }
}

@Preview
@Composable
private fun MainScreenPrev() {
    MainScreen(navController = rememberNavController())
}