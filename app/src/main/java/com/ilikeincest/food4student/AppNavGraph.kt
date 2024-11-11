package com.ilikeincest.food4student

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ilikeincest.food4student.screen.account_center.AccountCenterScreen
import com.ilikeincest.food4student.screen.auth.sign_in.SignInScreen
import com.ilikeincest.food4student.screen.auth.sign_up.SignUpScreen
import com.ilikeincest.food4student.screen.main_page.MainScreen
import com.ilikeincest.food4student.screen.map.MapScreen
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
    NavHost(
        navController = navController,
        startDestination = AppRoutes.SPLASH_SCREEN.name
    ) {
        composable(AppRoutes.SPLASH_SCREEN.name){
            SplashScreen(navController)
        }
        composable(AppRoutes.MAIN.name) {
            MainScreen({ navController.navigate(AppRoutes.MAP.name) }, { navController.navigate(AppRoutes.PROFILE.name) })
        }
        composable(AppRoutes.SIGN_IN.name) {
            Surface {
                SignInScreen(
                    navController = navController,
                    navigateToSignUp = { navController.navigate(AppRoutes.SIGN_UP.name) }
                )
            }
        }
        composable(AppRoutes.SIGN_UP.name){
            Surface {
                SignUpScreen(
                    navController = navController,
                    navigateToSignIn = { navController.navigate(AppRoutes.SIGN_IN.name) }
                )
            }
        }
        composable(AppRoutes.MAP.name) {
            MapScreen()
        }
        composable(AppRoutes.PROFILE.name) {
            AccountCenterScreen(navController = navController)
        }
    }
}