package com.ilikeincest.food4student

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ilikeincest.food4student.screen.account_center.AccountCenterScreen
import com.ilikeincest.food4student.screen.auth.sign_in.SignInScreen
import com.ilikeincest.food4student.screen.auth.sign_up.SignUpScreen
import com.ilikeincest.food4student.screen.main_page.MainScreen
import com.ilikeincest.food4student.screen.map.MapScreen
import com.ilikeincest.food4student.screen.shipping_location.ShippingLocationScreen
import com.ilikeincest.food4student.screen.splash.SplashScreen

enum class AppRoutes {
    MAIN,
    SIGN_IN,
    SIGN_UP,
    PROFILE,
    SHIPPING_LOCATION,
    PICK_LOCATION,
    MAP,
    SPLASH_SCREEN,
}

@Composable
fun AppNavGraph(navController: NavHostController = rememberNavController()) {
    val bgColor = if (isSystemInDarkTheme()) {
        Color.Black
    } else {
        Color.White
    }
    NavHost(
        navController = navController,
        enterTransition = { slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }) + fadeIn() },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth }) + fadeIn() },
        exitTransition = { slideOutHorizontally(targetOffsetX = { fullWidth -> -fullWidth }) + fadeOut() },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }) + fadeOut() },
        startDestination = AppRoutes.SPLASH_SCREEN.name,
        modifier = Modifier.background(color = bgColor)
    ) {
        composable(AppRoutes.SPLASH_SCREEN.name){
            SplashScreen(navController)
        }
        composable(AppRoutes.MAIN.name) {
            MainScreen(
                onNavigateToAccountCenter = { navController.navigate(AppRoutes.PROFILE.name) },
                onNavigateToShippingLocation = { navController.navigate(AppRoutes.SHIPPING_LOCATION.name) }
            )
        }
        composable(AppRoutes.SIGN_IN.name) {
            Surface {
                SignInScreen(
                    navController = navController,
                    onNavigateToSignUp = { navController.navigate(AppRoutes.SIGN_UP.name) }
                )
            }
        }
        composable(AppRoutes.SIGN_UP.name){
            Surface {
                SignUpScreen(
                    navController = navController,
                    onNavigateToSignIn = { navController.navigate(AppRoutes.SIGN_IN.name) }
                )
            }
        }
        composable(AppRoutes.SHIPPING_LOCATION.name) {
            ShippingLocationScreen(
                locationList = listOf(), // TODO: move to vm
                onNavigateUp = { navController.navigateUp() },
                onPickFromMap = { navController.navigate(AppRoutes.MAP.name) }
            )
        }
        composable(AppRoutes.MAP.name) {
            MapScreen(onNavigateUp = { navController.navigateUp() })
        }
        composable(AppRoutes.PROFILE.name) {
            AccountCenterScreen(navController = navController)
        }
    }
}