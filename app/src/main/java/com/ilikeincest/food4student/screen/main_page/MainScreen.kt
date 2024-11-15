package com.ilikeincest.food4student.screen.main_page

import android.widget.Toast
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ilikeincest.food4student.screen.main_page.component.GlobalSearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToMap: () -> Unit,
    onNavigateToAccountCenter: () -> Unit,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route?.let { route ->
        MainRoutes.entries.find { it.name == route }
    } ?: MainRoutes.HOME

    // TODO: add logic to populate this on load and maybe move to viewmodel
    val badgeInNavBar = remember { mutableStateMapOf<MainRoutes, String>(
        Pair(MainRoutes.ORDER, "5"),
        Pair(MainRoutes.NOTIFICATION, "3")
    ) }

    val routesWithoutSearchBar = listOf(MainRoutes.NOTIFICATION)
    val isRouteWithSearchBar = !routesWithoutSearchBar.contains(currentRoute)

    Scaffold(
        bottomBar = { NavigationBar {
            for (route in MainRoutes.entries) {
                val isSelected = route == currentRoute
                val icon = if (isSelected) route.selectedIcon
                    else route.unselectedIcon
                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        if (currentRoute != route)
                            navController.navigate(route.name)
                    },
                    label = { Text(stringResource(route.labelResId)) },
                    icon = {
                        val badge = badgeInNavBar[route]
                        if (badge != null) {
                            BadgedBox(
                                badge = { Badge { Text(badge) } }
                            ) { icon() }
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
            val context = LocalContext.current
            TopAppBar(
                title = { Text("Notification") },
                modifier = Modifier.alpha(topBarAlpha),
                actions = {
                    // TODO: add read all button logic
                    IconButton(onClick = {
                        Toast.makeText(context, "Mark all as read", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(Icons.Filled.Checklist, "Mark all as read")
                    }
                },
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
        val searchBarHeight = 76.dp
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
        MainScreenNavGraph(
            navController = navController,
            onNavigateToMap = onNavigateToMap,
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