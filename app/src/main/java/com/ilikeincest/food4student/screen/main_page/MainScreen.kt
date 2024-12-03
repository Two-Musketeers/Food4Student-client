package com.ilikeincest.food4student.screen.main_page

import android.Manifest
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.ilikeincest.food4student.screen.main_page.component.GlobalSearchBar
import com.ilikeincest.food4student.screen.main_page.notification.NotificationScreenViewModel

@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun RequestNotificationPermissionDialog() {
    val permissionState = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

    if (!permissionState.status.isGranted) {
//        if (permissionState.status.shouldShowRationale) RationaleDialog()
        LaunchedEffect(Unit) {
            permissionState.launchPermissionRequest()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToShippingLocation: () -> Unit,
    onNavigateToAccountCenter: () -> Unit,
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        RequestNotificationPermissionDialog()
    }

    var currentRoute by rememberSaveable { mutableStateOf(defaultRoute) }

    // TODO: add logic to populate this on load and maybe move to viewmodel
    val badgeInNavBar = remember { mutableStateMapOf<MainRoutes, String>(
        Pair(MainRoutes.ORDER, "5"),
        Pair(MainRoutes.NOTIFICATION, "3")
    ) }

    val routesWithoutSearchBar = listOf(MainRoutes.NOTIFICATION)
    val isRouteWithSearchBar = !routesWithoutSearchBar.contains(currentRoute)
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    BackHandler(enabled = currentRoute != defaultRoute) {
        currentRoute = defaultRoute
    }

    Scaffold(
        bottomBar = { NavigationBar {
            for (route in MainRoutes.entries) {
                val isSelected = route == currentRoute
                val iconRes = if (isSelected) route.selectedIcon
                    else route.unselectedIcon
                val icon = @Composable {
                    Icon(iconRes, null)
                }
                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        if (currentRoute != route)
                            currentRoute = route
                    },
                    label = { Text(stringResource(route.labelResId)) },
                    icon = {
                        val badge = badgeInNavBar[route]
                        if (badge != null) {
                            BadgedBox({ Badge { Text(badge) } }) { icon() }
                        } else { icon() }
                    }
                )
            }
        } },
        // show top bar if route is notification
        topBar = {
            val topBarAlpha by animateFloatAsState(
                targetValue = if (currentRoute == MainRoutes.NOTIFICATION) 1f else 0f,
                label = "Top bar alpha"
            )
            val topBarHeight by animateDpAsState(
                targetValue = if (currentRoute == MainRoutes.NOTIFICATION)
                    TopAppBarDefaults.TopAppBarExpandedHeight else 0.dp,
                label = "Top bar height"
            )
            TopAppBar(
                title = { Text("Notification") },
                modifier = Modifier.alpha(topBarAlpha),
                actions = {
                    val vm: NotificationScreenViewModel = hiltViewModel()
                    IconButton(onClick = { vm.markAllAsRead() }) {
                        Icon(Icons.Filled.Checklist, "Mark all as read")
                    }
                },
                scrollBehavior = scrollBehavior,
                expandedHeight = topBarHeight,
            )
        },
    ) { innerPadding ->
        // Animations
        // Search bar expand animation
        var expanded by remember { mutableStateOf(false) }
        val animatedContentOffset by animateIntOffsetAsState(
            targetValue = if (expanded) IntOffset(0, 100) else IntOffset(0, 0),
            label = "Search bar expanded content offset"
        )
        // Search bar visibility animation (navigate to routes with/without search bar)
        val animatedSearchBarOffset by animateIntOffsetAsState(
            targetValue =
                if (isRouteWithSearchBar) IntOffset(0, 0) else IntOffset(0, -100),
            label = "Search bar animated offset"
        )
        val searchBarHeight = 64.dp
        val animatedContentPaddingSearchBar by animateDpAsState(
            targetValue = if (isRouteWithSearchBar) searchBarHeight else 0.dp,
            label = "Search bar animated content padding"
        )
        val animatedSearchBarAlpha by animateFloatAsState(
            targetValue = if (isRouteWithSearchBar) 1f else 0f,
            label = "Search bar animated alpha"
        )

        // Search bar on top
        GlobalSearchBar(
            // TODO: replace with actual data
            userName = "Hồ Nguyên Minh",
            userAvatarUrl = "",
            onAvatarClicked = onNavigateToAccountCenter,
            modifier = Modifier
                .offset { animatedSearchBarOffset }
                .alpha(animatedSearchBarAlpha),
            onExpandedChange = { expanded = it },
            isVisible = animatedSearchBarOffset != IntOffset(0, 0)
        )

        // Main screen content
        MainScreenPageGraph(
            currentRoute = currentRoute,
            onNavigateToShippingLocation = onNavigateToShippingLocation,
            scrollConnection = scrollBehavior.nestedScrollConnection,
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = animatedContentPaddingSearchBar)
                .offset { animatedContentOffset }
        )
    }
}

@Preview
@Composable
private fun MainScreenPrev() {
    MainScreen({}, {})
}