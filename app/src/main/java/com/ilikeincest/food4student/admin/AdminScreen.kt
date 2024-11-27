package com.ilikeincest.food4student.admin

import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddModerator
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ilikeincest.food4student.admin.viewmodel.UserViewModel
import com.ilikeincest.food4student.model.User
import com.ilikeincest.food4student.R
import com.ilikeincest.food4student.screen.account_center.AccountCenterScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    viewModel: UserViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        bottomBar = {
            AdminNavigationBar(navController)
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            AdminNavGraph(navController, viewModel)
        }
    }

    // Fetch users when the screen is first displayed
    LaunchedEffect(Unit) {
        viewModel.fetchUsers(pageNumber = 1, pageSize = 30)
    }
}

@Composable
fun AdminNavigationBar(navController: NavHostController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar {
        val routes = listOf(AdminRoutes.Users, AdminRoutes.UnapprovedRestaurants, AdminRoutes.Account)
        routes.forEach { route ->
            NavigationBarItem(
                icon = { Icon(route.icon, contentDescription = null) },
                label = { Text(stringResource(route.labelResId)) },
                selected = currentRoute == route.route,
                onClick = {
                    if (currentRoute != route.route) {
                        navController.navigate(route.route) {
                            popUpTo(AdminRoutes.Users.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun UsersScreen(viewModel: UserViewModel) {
    val users = viewModel.users
    val searchQuery = viewModel.searchQuery

    Column {
        // Search Bar
        TextField(
            value = searchQuery,
            onValueChange = { newQuery -> viewModel.updateSearchQuery(newQuery) },
            label = { Text("Search by ID, Name, or Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            singleLine = true,
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
            }
        )

        // User List
        LazyColumn {
            items(users) { user ->
                UserItem(user)
            }
        }
    }
}

@Composable
fun UserItem(user: User) {
    var showDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDialog = true }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Icon(
            imageVector = getRoleIcon(user.role),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = "Id: ${user.id}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Name: ${user.displayName}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Email: ${user.email}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Role: ${user.role}", style = MaterialTheme.typography.bodySmall)
        }
    }

    if (showDialog) {
        UserActionDialog(
            user = user,
            onDismiss = { showDialog = false },
            viewModel = hiltViewModel()
        )
    }
}

@Composable
fun UserActionDialog(
    user: User,
    onDismiss: () -> Unit,
    viewModel: UserViewModel
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("User Actions") },
        text = { Text("Select an action for ${user.displayName ?: "this user"}") },
        confirmButton = {
            Column {
                // Ban or Unban User
                val isBanned = user.role == "Banned"
                TextButton(onClick = {
                    viewModel.updateUserRole(user.id, if (isBanned) "User" else "Banned")
                    onDismiss()
                    Toast.makeText(
                        context,
                        if (isBanned) "User Unbanned" else "User Banned",
                        Toast.LENGTH_SHORT
                    ).show()
                }) {
                    Text(if (isBanned) "Unban User" else "Ban User")
                }

                // Grant or Revoke Moderator Role
                when (user.role) {
                    "User" -> {
                        TextButton(onClick = {
                            viewModel.updateUserRole(user.id, "Moderator")
                            onDismiss()
                            Toast.makeText(context, "Granted Moderator Role", Toast.LENGTH_SHORT).show()
                        }) {
                            Text("Grant Moderator Role")
                        }
                    }
                    "Moderator" -> {
                        TextButton(onClick = {
                            viewModel.updateUserRole(user.id, "User")
                            onDismiss()
                            Toast.makeText(context, "Revoked Moderator Role", Toast.LENGTH_SHORT).show()
                        }) {
                            Text("Revoke Moderator Role")
                        }
                    }
                    else -> { /* Do nothing for other roles */ }
                }

                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        },
        dismissButton = {}
    )
}
fun getRoleIcon(role: String): ImageVector {
    return when (role) {
        "Admin" -> Icons.Filled.Android
        "User" -> Icons.Filled.Person
        "RestaurantOwner" -> Icons.Filled.Restaurant
        "Moderator" -> Icons.Filled.AddModerator
        "Banned" -> Icons.Filled.Block
        else -> Icons.Filled.Error
    }
}

sealed class AdminRoutes(val route: String, val icon: ImageVector, @StringRes val labelResId: Int) {
    object Users : AdminRoutes("users", Icons.Default.Person, R.string.users)
    object UnapprovedRestaurants : AdminRoutes("unapproved_restaurants", Icons.Default.Restaurant, R.string.unapproved_restaurants)
    object Account : AdminRoutes("account", Icons.Default.AccountCircle, R.string.account)
}

@Composable
fun AdminNavGraph(navController: NavHostController, viewModel: UserViewModel) {
    NavHost(
        navController = navController,
        startDestination = AdminRoutes.Users.route
    ) {
        composable(AdminRoutes.Users.route) {
            UsersScreen(viewModel)
        }
        composable(AdminRoutes.UnapprovedRestaurants.route) {
            UnapprovedRestaurantsScreen()
        }
        composable(AdminRoutes.Account.route) {
            AccountCenterScreen(navController = navController)
        }
    }
}

@Composable
fun UnapprovedRestaurantsScreen() {
    Text("Unapproved Restaurants Screen")
}