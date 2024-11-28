package com.ilikeincest.food4student.admin.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ilikeincest.food4student.admin.component.AdminNavGraph
import com.ilikeincest.food4student.admin.component.AdminNavigationBar
import com.ilikeincest.food4student.admin.viewmodel.AdminRestaurantViewModel
import com.ilikeincest.food4student.admin.viewmodel.AdminUserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    adminUserViewModel: AdminUserViewModel = hiltViewModel(),
    adminRestaurantViewModel: AdminRestaurantViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        bottomBar = {
            AdminNavigationBar(navController)
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            AdminNavGraph(navController, adminUserViewModel, adminRestaurantViewModel)
        }
    }
}