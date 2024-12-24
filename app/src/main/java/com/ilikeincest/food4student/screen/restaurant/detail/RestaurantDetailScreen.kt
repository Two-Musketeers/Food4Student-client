package com.ilikeincest.food4student.screen.restaurant.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.ilikeincest.food4student.R
import com.ilikeincest.food4student.component.BetterPullToRefreshBox
import com.ilikeincest.food4student.component.ErrorDialog
import com.ilikeincest.food4student.component.LoadingDialog
import com.ilikeincest.food4student.component.preview_helper.ScreenPreview
import com.ilikeincest.food4student.model.FoodCategory
import com.ilikeincest.food4student.model.FoodItem
import com.ilikeincest.food4student.model.Variation
import com.ilikeincest.food4student.model.VariationOption
import com.ilikeincest.food4student.screen.restaurant.detail.component.AddToCartSheet
import com.ilikeincest.food4student.screen.restaurant.detail.component.CartBottomSheet
import com.ilikeincest.food4student.screen.restaurant.detail.component.FoodItemCard
import com.ilikeincest.food4student.screen.restaurant.detail.component.RestaurantHeader
import com.ilikeincest.food4student.screen.restaurant.detail.component.ShoppingCartBottomBar
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun RestaurantScreen(
    onNavigateUp: () -> Unit,
    onNavigateToRating: (id: String) -> Unit,
    viewModel: RestaurantDetailViewModel = hiltViewModel()
) {
    val restaurantDetail by viewModel.restaurantDetail.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val showLoading by viewModel.showLoading.collectAsState()

    if (errorMessage.isNotEmpty()) {
        ErrorDialog(
            message = errorMessage,
            onDismiss = { viewModel.dismissError() }
        )
    }
    if (showLoading) {
        LoadingDialog(
            label = "Đang tải nhà hàng bạn chờ tí nha... ヾ(≧▽≦*)o",
            isVisible = false
        )
    }

    // for now.
    restaurantDetail?.let { detail ->
        val distance = "${String.format("%.2f", detail.distanceInKm)} km"
        val timeAway = "${detail.estimatedTimeInMinutes} phút"
        RestaurantScreenContent(
            bannerModel = detail.bannerUrl,
            name = detail.name,
            starRating = detail.averageRating.toString(),
            distance = distance,
            timeAway = timeAway,
            description = detail.description,
            onNavigateUp = onNavigateUp,
            onNavigateToRating = onNavigateToRating,
            viewModel = viewModel
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun RestaurantScreenContent(
    bannerModel: Any?,
    name: String,
    starRating: String,
    distance: String,
    timeAway: String,
    description: String?,
    onNavigateUp: () -> Unit,
    onNavigateToRating: (id: String) -> Unit,
    viewModel: RestaurantDetailViewModel
) {
    val restaurantDetail = viewModel.restaurantDetail.collectAsState().value
    val cartItems by viewModel.cartItems.collectAsState()

    val foodList = restaurantDetail?.foodCategories ?: emptyList()
    var isRefreshing by remember { mutableStateOf(false) }
    var addToCartItem by remember { mutableStateOf<FoodItem?>(null) }
    var openCart by remember { mutableStateOf(false) }

    if (openCart) {
        CartBottomSheet(
            onDismiss = {
                openCart = false
            },
            viewModel = viewModel
        )
    }

    // view states
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val lazyListState = rememberLazyListState()

    val firstIndex by remember { derivedStateOf { lazyListState.firstVisibleItemIndex } }
    val showTitle by remember { derivedStateOf { firstIndex >= 1 } }
    val isDragged by lazyListState.interactionSource.collectIsDraggedAsState()

    val tabBarHeightPx = with(LocalDensity.current) { 48.dp.toPx() }
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

    val topBarBg by animateColorAsState(
        if (showTitle) colorScheme.surfaceContainer else Color.Transparent
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    AnimatedVisibility(
                        showTitle,
                        enter = slideInVertically() + fadeIn(),
                        exit = slideOutVertically() + fadeOut()
                    ) { Text(name) }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = topBarBg
                ),
                navigationIcon = {
                    Row {
                        AnimatedVisibility(!showTitle) { Spacer(Modifier.width(8.dp)) }
                        IconButton(
                            onClick = onNavigateUp,
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = colorScheme.surfaceContainer
                            )
                        ) {
                            Icon(Icons.AutoMirrored.Default.ArrowBack, null)
                        }
                    }
                },
            )
        },
        contentWindowInsets = WindowInsets.statusBars,
        bottomBar = {
            if (cartItems.isNotEmpty()) {
                ShoppingCartBottomBar(
                    viewModel = viewModel,
                    onCartClick = {
                        openCart = true
                    }
                )
            }
        }
    ) { innerPadding ->
        AsyncImage(
            model = bannerModel,
            contentDescription = "Restaurant banner",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f)
        )

        val bgModifier = Modifier
            .background(colorScheme.surface)
            .fillMaxWidth()
        BetterPullToRefreshBox(
            lazyListState = lazyListState,
            isRefreshing = isRefreshing,
            onRefresh = {
                // TODO
                isRefreshing = false
            },
        ) {
            LazyColumn(
                state = lazyListState,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                item(contentType = "spacing box") {
                    // calculate spaced height to ensure picture is shown with 1:2 ratio
                    val remainingHeight = calculateRemainingBannerHeight(innerPadding)
                    Spacer(Modifier.height(remainingHeight))
                }

                // header
                item(contentType = "restaurant header") {
                    RestaurantHeader(
                        name = name,
                        starRating = starRating,
                        distance = distance,
                        timeAway = timeAway,
                        description = description,
                        isFavorite = restaurantDetail!!.isFavorited,
                        onFavoriteToggle = {
                            viewModel.toggleLike(restaurantDetail.id)
                        },
                        onNavigateToRating = {
                            onNavigateToRating(restaurantDetail.id)
                        },
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
                                .background(colorScheme.surface)) {
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
                        contentType = { "food item card" }
                    ) {
                        FoodItemCard(
                            item = it,
                            modifier = bgModifier.padding(16.dp),
                            // TODO: add proper edit layout if have time
                            onDecreaseInCart = null, // disable the button
                            onIncreaseInCart = { addToCartItem = it },
                            inCartCount = 0, // TODO
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

                item { Spacer(Modifier.height(32.dp)) } // some space to breath
            }
        }
    }

    if (addToCartItem != null) {
        AddToCartSheet(
            item = addToCartItem!!,
            onDismiss = { addToCartItem = null },
            viewModel = viewModel
            // TODO
        )
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