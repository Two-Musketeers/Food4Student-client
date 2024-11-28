package com.ilikeincest.food4student.admin.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddModerator
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ilikeincest.food4student.admin.viewmodel.AdminUserViewModel
import com.ilikeincest.food4student.admin.component.AdminNavGraph
import com.ilikeincest.food4student.admin.component.AdminNavigationBar
import com.ilikeincest.food4student.admin.viewmodel.AdminRestaurantViewModel

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

    // Fetch users when the screen is first displayed
    LaunchedEffect(Unit) {
        adminUserViewModel.fetchUsers(pageNumber = 1, pageSize = 30)
    }
}