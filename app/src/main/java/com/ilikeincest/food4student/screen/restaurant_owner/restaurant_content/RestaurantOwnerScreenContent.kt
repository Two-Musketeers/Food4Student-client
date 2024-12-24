package com.ilikeincest.food4student.screen.restaurant_owner.restaurant_content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.ilikeincest.food4student.component.ErrorDialog
import com.ilikeincest.food4student.screen.restaurant_owner.RestaurantOwnerViewModel
import com.ilikeincest.food4student.screen.restaurant_owner.component.FoodItemOwnerCard
import com.ilikeincest.food4student.screen.restaurant_owner.component.RestaurantOwnerHeader
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun RestaurantOwnerScreenContent(
    onNavigateToAddEditFoodItem: () -> Unit,
    viewModel: RestaurantOwnerViewModel
) {
    val restaurantState = viewModel.restaurant.collectAsState()
    val restaurant = restaurantState.value
    val foodList = restaurant?.foodCategories ?: emptyList()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val lazyListState = rememberLazyListState()
    val firstIndex by remember { derivedStateOf { lazyListState.firstVisibleItemIndex } }
    val showTitle by remember { derivedStateOf { firstIndex >= 1 } }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val tabBarHeightPx = with(LocalDensity.current) { 48.dp.toPx() }
    val isDragged by lazyListState.interactionSource.collectIsDraggedAsState()

    if (errorMessage.isNotEmpty()) {
        ErrorDialog(
            message = errorMessage,
            onDismiss = { viewModel.dismissErrorDialog() }
        )
    }
    if (isDragged) {
        LaunchedEffect(firstIndex) {
            var current = 2
            if (firstIndex <= current) {
                selectedTab = 0
                return@LaunchedEffect
            }
            for (i in foodList.indices) {
                if (firstIndex > current) {
                    current += foodList[i].foodItems.size + 1
                    continue
                }
                if (firstIndex == current) {
                    selectedTab = i
                }
                if (firstIndex == current - 1) {
                    selectedTab = i - 1
                }
                return@LaunchedEffect
            }
        }
    }
    restaurant?.let {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        AnimatedVisibility(
                            showTitle,
                            enter = slideInVertically() + fadeIn(),
                            exit = slideOutVertically() + fadeOut()
                        ) { Text(restaurant.name) }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    navigationIcon = {},
                    scrollBehavior = scrollBehavior
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    viewModel.setSelectedFoodItem(null)
                    onNavigateToAddEditFoodItem()
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Thêm món ăn")
                }
            },
            contentWindowInsets = WindowInsets(0.dp),
        ) { innerPadding ->
            restaurant.let { restaurant ->
                AsyncImage(
                    model = restaurant.bannerUrl,
                    contentDescription = "Restaurant banner",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(2f)
                )

                val bgModifier = Modifier
                    .background(colorScheme.surface)
                    .fillMaxWidth()
                val foodItems = restaurant.foodCategories.flatMap { it.foodItems }
                if (foodItems.isNotEmpty()) {
                    LazyColumn(
                        state = lazyListState,
                        verticalArrangement = Arrangement.Top,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                    ) {
                        item(contentType = "spacing box") {
                            // calculate spaced height to ensure picture is shown with 1:2 ratio
                            val remainingHeight = calculateRemainingBannerHeight(innerPadding)
                            Spacer(Modifier.height(remainingHeight))
                        }

                        // header
                        item(contentType = "restaurant header") {
                            RestaurantOwnerHeader(
                                name = restaurant.name,
                                starRating = restaurant.averageRating.toString(),
                                description = restaurant.description,
                                modifier = bgModifier
                            )
                        }

                        // actual content starts at index 3
                        val categoryInitialIndex = 3
                        if (foodList.isNotEmpty()) {
                            stickyHeader(contentType = "category tabs") {
                                val coroutineScope = rememberCoroutineScope()
                                Box(
                                    Modifier
                                        .fillMaxWidth()
                                        .background(colorScheme.surface)
                                ) {
                                    SecondaryScrollableTabRow(selectedTabIndex = selectedTab) {
                                        foodList.forEachIndexed { i, category ->
                                            Tab(
                                                selected = selectedTab == i,
                                                text = { Text(category.name) },
                                                onClick = {
                                                    coroutineScope.launch {
                                                        selectedTab = i
                                                        var target = categoryInitialIndex
                                                        // each category has 1 header
                                                        // and n food item
                                                        for (cate in 0..<i) {
                                                            target += foodList[cate].foodItems.size + 1
                                                        }
                                                        lazyListState.animateScrollToItem(
                                                            target,
                                                            -tabBarHeightPx.roundToInt()
                                                        )
                                                    }
                                                },
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        foodList.forEach { category ->
                            item(
                                key = category.id,
                                contentType = "category header"
                            ) {
                                Text(
                                    "${category.name} (${category.foodItems.size})",
                                    style = typography.titleLarge.copy(
                                        fontSize = 22.sp,
                                    ),
                                    modifier = bgModifier
                                        .padding(horizontal = 16.dp)
                                        .padding(top = 16.dp)
                                )
                            }
                            items(
                                category.foodItems,
                                key = { it.id },
                                contentType = { "food item card" },
                            ) {
                                FoodItemOwnerCard(
                                    item = it,
                                    onClick = {
                                        viewModel.setSelectedFoodItem(it)
                                        onNavigateToAddEditFoodItem()
                                    },
                                    modifier = bgModifier.padding(16.dp)
                                )
                                HorizontalDivider(
                                    thickness = 1.dp,
                                    color = colorScheme.outlineVariant.copy(
                                        alpha = 0.4f
                                    ),
                                    modifier = bgModifier.padding(horizontal = 24.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

    } ?: run {
        Box(
            modifier = Modifier.Companion
                .fillMaxSize(),
            contentAlignment = Alignment.Companion.Center
        ) {
            CircularProgressIndicator()
        }


    }
}

@Composable
private fun calculateRemainingBannerHeight(innerPadding: PaddingValues): Dp {
    val configuration = LocalConfiguration.current
    val screenWidth = remember { configuration.screenWidthDp.dp }
    val targetHeight = remember { screenWidth / 2 }
    val heightBehindSystemUi = remember { innerPadding.calculateTopPadding() }
    val remainingHeight = remember { targetHeight - heightBehindSystemUi }
    return remainingHeight
}