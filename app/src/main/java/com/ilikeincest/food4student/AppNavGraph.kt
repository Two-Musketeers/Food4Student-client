package com.ilikeincest.food4student

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.navigation.toRoute
import com.ilikeincest.food4student.admin.screen.AdminScreen
import com.ilikeincest.food4student.dto.NoNeedToFetchAgainBuddy
import com.ilikeincest.food4student.dto.order.CreateOrderDto
import com.ilikeincest.food4student.dto.order.CreateOrderItemDto
import com.ilikeincest.food4student.model.Location
import com.ilikeincest.food4student.model.SavedShippingLocation
import com.ilikeincest.food4student.model.SavedShippingLocationType
import com.ilikeincest.food4student.screen.account_center.AccountCenterScreen
import com.ilikeincest.food4student.screen.auth.forget_password.ForgetPasswordScreen
import com.ilikeincest.food4student.screen.auth.select_role.SelectRoleRestaurantScreen
import com.ilikeincest.food4student.screen.auth.select_role.SelectRoleScreen
import com.ilikeincest.food4student.screen.auth.select_role.SelectRoleUserScreen
import com.ilikeincest.food4student.screen.auth.sign_in.SignInScreen
import com.ilikeincest.food4student.screen.auth.sign_up.SignUpScreen
import com.ilikeincest.food4student.screen.checkout.confirm.CheckoutConfirmScreen
import com.ilikeincest.food4student.screen.checkout.success.CheckoutSuccessScreen
import com.ilikeincest.food4student.screen.main_page.MainScreen
import com.ilikeincest.food4student.screen.restaurant.detail.Cart
import com.ilikeincest.food4student.screen.restaurant.detail.CartItem
import com.ilikeincest.food4student.screen.restaurant_owner.RestaurantOwnerScreen
import com.ilikeincest.food4student.screen.restaurant_owner.RestaurantOwnerViewModel
import com.ilikeincest.food4student.screen.restaurant_owner.food_item.add_category.AddCategoryScreen
import com.ilikeincest.food4student.screen.restaurant_owner.food_item.add_edit_saved_product.AddEditSavedFoodItemScreen
import com.ilikeincest.food4student.screen.restaurant_owner.food_item.add_edit_saved_varations.AddEditSavedVariationScreen
import com.ilikeincest.food4student.screen.restaurant.detail.RestaurantScreen
import com.ilikeincest.food4student.screen.restaurant.rating.RestaurantRatingScreen
import com.ilikeincest.food4student.screen.shipping.add_edit_saved_location.AddEditSavedLocationScreen
import com.ilikeincest.food4student.screen.shipping.pick_location.MapScreen
import com.ilikeincest.food4student.screen.shipping.shipping_location.ShippingLocationScreen
import com.ilikeincest.food4student.screen.splash.SplashScreen
import com.ilikeincest.food4student.util.nav.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object AppRoutes {
    @Serializable
    object Main

    // Auth
    @Serializable
    object SignIn
    @Serializable
    object SignUp
    @Serializable
    object ForgetPassword
    @Serializable
    object Profile
    @Serializable
    object SelectRole
    @Serializable
    object SignUpAsUser
    @Serializable
    object SignUpAsRestaurant

    @Serializable
    data class RestaurantDetail(
        val id: String,
        val distance: Double,
        val timeAway: Int,
        val isFavorited: Boolean
    )
    @Serializable
    data class RestaurantRating(val id: String)

    @Serializable
    object ShippingLocation
    @Serializable
    object PickLocation
    @Serializable
    data class AddSavedLocation(val type: SavedShippingLocationType)
    @Serializable
    data class EditSavedLocation(val id: String)

    @Serializable
    object Admin
    @Serializable
    object Restaurant
    @Serializable
    object AddEditFoodItem
    @Serializable
    object AddCategory
    @Serializable
    object AddEditVariation
    @Serializable
    object SplashScreen

    // checkout
    @Serializable
    data class CheckoutConfirm(val order: String) // is json of Cart
    @Serializable
    object CheckoutSuccess
}

@Composable
fun AppNavGraph(navController: NavHostController = rememberNavController()) {
    // to make the transition fade to/from this color, not from white always
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
        startDestination = AppRoutes.SplashScreen,
        modifier = Modifier.background(color = bgColor)
    ) {
        composable<AppRoutes.SplashScreen> {
            SplashScreen(
                onSetRootAdmin = { navController.navigateAsRootRoute(AppRoutes.Admin) },
                onSetRootMain = { navController.navigateAsRootRoute(AppRoutes.Main) },
                onSetRootSignIn = { navController.navigateAsRootRoute(AppRoutes.SignIn) },
                onSetRootSelectRole = { navController.navigateAsRootRoute(AppRoutes.SelectRole) },
                onSetRootRestaurant = { navController.navigateAsRootRoute(AppRoutes.Restaurant) }
            )
        }
        composable<AppRoutes.Main> {
            MainScreen(
                onNavigateToAccountCenter = { navController.navigate(AppRoutes.Profile) },
                onNavigateToShippingLocation = {
                    navController.navigate(AppRoutes.ShippingLocation)
                },
                onNavigateToRestaurant = { buddy ->
                    navController.navigate(AppRoutes.RestaurantDetail(
                        id = buddy.Id,
                        distance = buddy.Distance,
                        timeAway = buddy.TimeAway,
                        isFavorited = buddy.IsFavorited
                    ))
                }
            )
        }
        composable<AppRoutes.Admin> {
            AdminScreen()
        }
        navigation(
            route = "RestaurantFlow",
            startDestination = "${AppRoutes.Restaurant}"
        ) {
            composable<AppRoutes.Restaurant> {
                val parentEntry = remember(navController) {
                    navController.getBackStackEntry("RestaurantFlow")
                }
                val sharedViewModel = hiltViewModel<RestaurantOwnerViewModel>(parentEntry)
                RestaurantOwnerScreen(
                    viewModel = sharedViewModel,
                    onNavigateToAddEditFoodItem = { navController.navigate(AppRoutes.AddEditFoodItem) },
                    navController = navController
                )
            }
            composable<AppRoutes.AddEditFoodItem> {
                val parentEntry = remember(navController) {
                    navController.getBackStackEntry("RestaurantFlow")
                }
                val sharedViewModel = hiltViewModel<RestaurantOwnerViewModel>(parentEntry)
                AddEditSavedFoodItemScreen(
                    viewModel = sharedViewModel,
                    onNavigateUp = { navController.navigateUp() },
                    onNavigateToFoodCategory = { navController.navigate(AppRoutes.AddCategory) },
                    onNavigateToVariation = { navController.navigate(AppRoutes.AddEditVariation) }
                )
            }
            composable<AppRoutes.AddCategory> {
                val parentEntry = remember(navController) {
                    navController.getBackStackEntry("RestaurantFlow")
                }
                val sharedViewModel = hiltViewModel<RestaurantOwnerViewModel>(parentEntry)
                AddCategoryScreen(
                    viewModel = sharedViewModel,
                    onNavigateUp = { navController.navigateUp() }
                )
            }
            composable<AppRoutes.AddEditVariation> {
                val parentEntry = remember(navController) {
                    navController.getBackStackEntry("RestaurantFlow")
                }
                val sharedViewModel = hiltViewModel<RestaurantOwnerViewModel>(parentEntry)
                AddEditSavedVariationScreen(
                    viewModel = sharedViewModel,
                    onNavigateUp = { navController.navigateUp() }
                )
            }
        }
        composable<AppRoutes.SignIn> {
            SignInScreen(
                onSetRootSplash = { navController.navigateAsRootRoute(AppRoutes.SplashScreen) },
                onNavigateToSignUp = { navController.navigate(AppRoutes.SignUp) },
                onNavigateToForgetPassword = { navController.navigate(AppRoutes.ForgetPassword) }
            )
        }
        composable<AppRoutes.SignUp> {
            SignUpScreen(
                onSetRootSplash = { navController.navigateAsRootRoute(AppRoutes.SplashScreen) },
                onNavigateToSignIn = { navController.popBackStack(AppRoutes.SignIn, false) }
            )
        }
        composable<AppRoutes.ForgetPassword> {
            ForgetPasswordScreen(onNavigateUp = { navController.navigateUp() })
        }
        composable<AppRoutes.SelectRole> {
            SelectRoleScreen(
                onSignUpAsUser = { navController.navigate(AppRoutes.SignUpAsUser) },
                onSignUpAsRestaurant = { navController.navigate(AppRoutes.SignUpAsRestaurant) }
            )
        }
        composable<AppRoutes.SignUpAsUser> {
            SelectRoleUserScreen(
                onSuccessSignUp = { navController.navigateAsRootRoute(AppRoutes.SplashScreen) }
            )
        }
        composable<AppRoutes.SignUpAsRestaurant> {
            NavigateWithResult(it) { location: Location? ->
                SelectRoleRestaurantScreen(
                    selectedLocation = location,
                    onNavigateToLocationPicker = { navController.navigate(AppRoutes.PickLocation) },
                    onSetRootSplashScreen = { navController.navigateAsRootRoute(AppRoutes.SplashScreen) }
                )
            }
        }
        composable<AppRoutes.ShippingLocation> {
            NavigateWithResult(it) { location: Location? ->
                ShippingLocationScreen(
                    pickedLocation = location,
                    onNavigateUp = { navController.navigateUp() },
                    onPickFromMap = { navController.navigate(AppRoutes.PickLocation) },
                    onAddLocation = { navController.navigate(AppRoutes.AddSavedLocation(it)) },
                    onEditLocation = { navController.navigate(AppRoutes.EditSavedLocation(it)) }
                )
            }
        }
        composable<AppRoutes.PickLocation> {
            MapScreen(
                onNavigateUp = { navController.navigateUp() },
                onSelectLocation = { navController.popBackWithResult(it) }
            )
        }
        composable<AppRoutes.AddSavedLocation> {
            val route = it.toRoute<AppRoutes.AddSavedLocation>()
            NavigateWithResult(it) { location: Location? ->
                AddEditSavedLocationScreen(
                    selectedAddress = location,
                    onNavigateUp = { navController.navigateUp() },
                    onPickFromMap = { navController.navigate(AppRoutes.PickLocation) },
                    defaultType = route.type
                )
            }
        }
        composable<AppRoutes.EditSavedLocation> {
            val route = it.toRoute<AppRoutes.EditSavedLocation>()
            NavigateWithResult(it) { location: Location? ->
                AddEditSavedLocationScreen(
                    selectedAddress = location,
                    onNavigateUp = { navController.navigateUp() },
                    onPickFromMap = { navController.navigate(AppRoutes.PickLocation) },
                    id = route.id
                )
            }
        }
        composable<AppRoutes.Profile> {
            AccountCenterScreen(navController = navController)
        }

        // restaurant routes
        composable<AppRoutes.RestaurantDetail> {
            RestaurantScreen(
                onNavigateUp = { navController.navigateUp() },
                onNavigateToRating = { navController.navigate(AppRoutes.RestaurantRating(it)) },
                onNavigateToCheckout = {
                    val cart = Json.encodeToString(it)
                    navController.navigate(AppRoutes.CheckoutConfirm(cart))
                },
            )
        }
        composable<AppRoutes.RestaurantRating> {
            RestaurantRatingScreen(
                onNavigateUp = { navController.navigateUp() }
            )
        }

        // checkout
        composable<AppRoutes.CheckoutConfirm> {
            val route = it.toRoute<AppRoutes.CheckoutConfirm>()
            val order = remember { Json.decodeFromString<Cart>(route.order) }
            CheckoutConfirmScreen(
                order,
                onSuccess = { navController.navigate(AppRoutes.CheckoutSuccess) {
                    popUpTo<AppRoutes.RestaurantDetail> {
                        inclusive = true
                        saveState = false
                    }
                } },
                onNavigateToShippingLocation = { navController.navigate(AppRoutes.ShippingLocation) }
            )
        }

        composable<AppRoutes.CheckoutSuccess> {
            CheckoutSuccessScreen { navController.popBackStack(AppRoutes.Main, false) }
        }
    }
}