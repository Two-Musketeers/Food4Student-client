package com.ilikeincest.food4student.screen.restaurant.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.ilikeincest.food4student.R
import com.ilikeincest.food4student.component.BetterPullToRefreshBox
import com.ilikeincest.food4student.component.preview_helper.ScreenPreview
import com.ilikeincest.food4student.model.FoodCategory
import com.ilikeincest.food4student.model.FoodItem
import com.ilikeincest.food4student.model.Variation
import com.ilikeincest.food4student.model.VariationOption
import com.ilikeincest.food4student.screen.restaurant.detail.component.AddToCartSheet
import com.ilikeincest.food4student.screen.restaurant.detail.component.FoodItemCard
import com.ilikeincest.food4student.screen.restaurant.detail.component.RestaurantHeader
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun RestaurantScreen(
    onNavigateUp: () -> Unit,
    // TODO: add nav to order screen
    // TODO: add vm
) {
    // for now.
    RestaurantScreenContent(
        bannerModel = R.drawable.ic_launcher_background,
        name = "Hồng Trà Ngô Gia",
        starRating = "4.3",
        distance = "2.0km",
        timeAway = "25 phút",
        description = "Cửa hàng hồng trà nổi danh với \"bang for your bucks\", những ly trà 1 lít!" +
                " Ferox boreass ducunt ad index. Sunt competitiones vitare superbus, peritus hydraes.",
        onNavigateUp = onNavigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun RestaurantScreenContent(
    bannerModel: Any?,
    name: String,
    starRating: String,
    distance: String,
    timeAway: String,
    description: String,
    onNavigateUp: () -> Unit,
) {
    // vm states
    val foodList = remember { seedList() } // TODO: replace with vm
    var isRefreshing by remember { mutableStateOf(false) }
    var addToCartItem by remember { mutableStateOf<FoodItem?>(null) }
//    val cart = remember { mutableStateListOf<>() } // TODO

    // TODO handle the status bar
    // view states
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val lazyListState = rememberLazyListState()

    val firstIndex by remember { derivedStateOf { lazyListState.firstVisibleItemIndex } }
    val showTitle by remember { derivedStateOf { firstIndex >= 1 } }
    val isDragged by lazyListState.interactionSource.collectIsDraggedAsState()

    val tabBarHeightPx = with(LocalDensity.current) { 48.dp.toPx() }
    if (isDragged) {
        LaunchedEffect(firstIndex) {
            var current = 2
            if (firstIndex <= current)  {
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

    Scaffold(
        topBar = { TopAppBar(
            title = {
                AnimatedVisibility(showTitle,
                    enter = slideInVertically() + fadeIn(),
                    exit = slideOutVertically() + fadeOut()
                ) { Text(name) }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            ),
            navigationIcon = {
                Row {
                    AnimatedVisibility(!showTitle) { Spacer(Modifier.width(8.dp)) }
                    IconButton(onClick = onNavigateUp,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = colorScheme.surfaceContainer
                        )
                    ) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, null)
                    }
                }
            },
            scrollBehavior = scrollBehavior
        ) },
    ) { innerPadding ->
        AsyncImage(
            model = bannerModel,
            contentDescription = "Restaurant banner",
            contentScale = ContentScale.FillWidth,
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
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
            ) {
                item(contentType = "spacing box") {
                    // calculate spaced height to ensure picture is shown with 1:2 ratio
                    val remainingHeight = calculateRemainingBannerHeight(innerPadding)
                    Spacer(Modifier.height(remainingHeight))
                }

                // header
                item(contentType = "restaurant header") {
                    var isFavorite by remember { mutableStateOf(false) } // TODO
                    RestaurantHeader(
                        name = name,
                        starRating = starRating,
                        distance = distance,
                        timeAway = timeAway,
                        description = description,
                        isFavorite = isFavorite,
                        onFavoriteToggle = { isFavorite = !isFavorite },
                        modifier = bgModifier
                    )
                }

                // actual content starts at index 3
                val categoryInitialIndex = 3
                if (foodList.isNotEmpty()) {
                    stickyHeader(contentType = "category tabs") {
                        val coroutineScope = rememberCoroutineScope()
                        SecondaryScrollableTabRow(selectedTabIndex = selectedTab) {
                            foodList.forEachIndexed { i, category -> Tab(
                                selected = selectedTab == i,
                                text = { Text(category.name) },
                                onClick = { coroutineScope.launch {
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
                                } },
                            ) }
                        }
                    }
                }

                foodList.forEach { category ->
                    item(
                        key = category.name,
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
                            inCartCount = 0 // TODO
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

    if (addToCartItem != null) {
        AddToCartSheet(
            item = addToCartItem!!,
            onDismiss = { addToCartItem = null },
            onAddItemToCart = {}, // TODO
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Prev() { ScreenPreview {
    RestaurantScreenContent(
        R.drawable.ic_launcher_background,
        "Hồng Trà Ngô Gia",
        "4.3",
        "2.0km",
        "25 phút",
        "Cửa hàng hồng trà nổi danh với \"bang for your bucks\", những ly trà 1 lít!" +
                " Ferox boreass ducunt ad index. Sunt competitiones vitare superbus, peritus hydraes.",
        onNavigateUp = {}
    )
} }

@Composable
private fun calculateRemainingBannerHeight(innerPadding: PaddingValues): Dp {
    val configuration = LocalConfiguration.current
    val screenWidth = remember { configuration.screenWidthDp.dp }
    val targetHeight = remember { screenWidth / 2 }
    val heightBehindSystemUi = remember { innerPadding.calculateTopPadding() }
    val remainingHeight = remember { targetHeight - heightBehindSystemUi }
    return remainingHeight
}

private fun seedFood(seed: Int): List<FoodItem> {
    val r = Random(seed)
    return List(5) {
        val id = r.nextInt()
        FoodItem(
            id = id.toString(),
            name = "Hồng Trà Kem Tươi",
            description =
                if (id.mod(2) == 0)
                    "Est sed takimata consetetur enim ipsum eos quis diam gubergren. Clita placerat nobis invidunt dolore et dolor amet erat accusam ea accusam sed justo erat autem praesent. Qui rebum sit velit vel dolore stet et nulla placerat dolore gubergren stet laoreet option lorem autem invidunt invidunt. Kasd elit enim consectetuer dolor tempor diam takimata ea elitr esse eros odio esse velit ut stet. Sanctus nonumy eos et et sanctus possim feugiat. Takimata at vero hendrerit sadipscing nulla doming ea nonumy duis ipsum. Dolore consequat magna justo dolore dolor justo doming sit tempor et invidunt aliquyam magna sit. Amet takimata accumsan dolor dignissim te dolor et dolor erat imperdiet duo kasd et eleifend dolore. Diam mazim eirmod et feugiat labore ipsum et invidunt magna et ut. Diam clita exerci clita. Dolore at est soluta aliquam ipsum nulla feugiat dolores at sit ut at suscipit duo consetetur. Tation tincidunt lorem ea aliquip et te wisi ad dolore sed clita. Sit at dolore sit duo nulla tempor at. Labore feugait lorem lobortis consequat. Ut aliquyam ea eos wisi vero ut et stet sanctus duis est sit. No labore eu duo."
                else "Kem có tan chảy",
            foodItemPhotoUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR58QY4pAtehHlOZtYU0gDSbTABNIsxy2z_gQ&s",
            basePrice = 23000,
            variations = listOf(Variation(
                id = "lmao",
                name = "Size",
                minSelect = 1,
                maxSelect = 1,
                variationOptions = listOf(
                    VariationOption(
                        id = Random.nextInt().toString(),
                        name = "S",
                        priceAdjustment = 0
                    ),
                    VariationOption(
                        id = Random.nextInt().toString(),
                        name = "M",
                        priceAdjustment = 3000
                    ),
                    VariationOption(
                        id = Random.nextInt().toString(),
                        name = "L",
                        priceAdjustment = 6000
                    )
                )
            ))
        )
    }
}
private fun seedList(): List<FoodCategory> {
    val r = Random(21)
    val cats = listOf("THỨC UỐNG HOT", "TRÀ TRÁI CÂY", "THUẦN TRÀ", "TRÀ LATTE", "TRÀ SỮA", "TRÀ CHANH")
    return List(cats.size) {
        FoodCategory(
            id = r.nextInt().toString(),
            name = cats[it],
            foodItems = seedFood(r.nextInt()),
            restaurantId = "wtf"
        )
    }
}