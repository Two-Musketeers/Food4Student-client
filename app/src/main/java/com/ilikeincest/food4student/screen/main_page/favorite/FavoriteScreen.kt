package com.ilikeincest.food4student.screen.main_page.favorite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.here.sdk.core.GeoCoordinates
import com.ilikeincest.food4student.component.ErrorDialog
import com.ilikeincest.food4student.dto.NoNeedToFetchAgainBuddy
import com.ilikeincest.food4student.screen.main_page.component.ShopListingCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
    vm: FavoriteViewModel = hiltViewModel(),
    currentLocation: GeoCoordinates?,
    onNavigateToRestaurant: (noNeedToFetchAgainBuddy: NoNeedToFetchAgainBuddy) -> Unit
) {
    val restaurantList = vm.restaurantList
    val errorMessage by vm.errorMessage.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val isLoadingMore by vm.isLoadingMore.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }
    val noMoreRestaurant by vm.noMoreRestaurant.collectAsState()

    LaunchedEffect(currentLocation) {
        if (currentLocation != null && restaurantList.isEmpty()) {
            vm.refreshFavorites(currentLocation.latitude, currentLocation.longitude)
        }
    }

    if (errorMessage.isNotEmpty()) {
        ErrorDialog(
            message = errorMessage,
            onDismiss = { vm.dismissError() },
        )
    }

    PullToRefreshBox(isRefreshing, {
        if (currentLocation != null) {
            coroutineScope.launch {
                vm.refreshFavorites(currentLocation.latitude, currentLocation.longitude)
                isRefreshing = false
            }
        }
    }) {
        if (restaurantList.isEmpty()) {
            // Show empty state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Bạn chưa thích nhà hàng nào hết á ( •̀ ω •́ )✧.",
                    style = typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = modifier.padding(horizontal = 16.dp)
            ) {
                item {} // to get the default 16dp spacing as padding
                items(restaurantList, key = { it.id }) { restaurant ->
                    val noNeedToFetchAgainBuddy = NoNeedToFetchAgainBuddy(
                        Id = restaurant.id,
                        TimeAway = restaurant.estimatedTimeInMinutes,
                        Distance = restaurant.distanceInKm,
                        IsFavorited = restaurant.isFavorited
                    )
                    ShopListingCard(
                        shopName = restaurant.name,
                        starRating = restaurant.averageRating.toString(),
                        distance = "${String.format("%.2f", restaurant.distanceInKm)} km",
                        timeAway = "${restaurant.estimatedTimeInMinutes} phút",
                        shopImageModel = restaurant.logoUrl, // TODO
                        isFavorite = restaurant.isFavorited, // disable favorite on home page
                        onFavoriteChange = { vm.toggleLike(restaurant.id) },
                        onClick = { onNavigateToRestaurant(noNeedToFetchAgainBuddy) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
                item {
                    LaunchedEffect(isLoadingMore, noMoreRestaurant, isRefreshing) {
                        if (isLoadingMore || noMoreRestaurant || isRefreshing)
                            return@LaunchedEffect
                        currentLocation?.let {
                            vm.loadMoreFavorites(it)
                        }
                    }
                    if (noMoreRestaurant) {
                        Text(
                            "Hết nhà hàng yêu thích rồi bạn ơi (っ °Д °;)っ",
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            CircularProgressIndicator(
                                Modifier
                                    .padding(16.dp)
                                    .size(32.dp),
                            )
                            Text("Đang tải thêm nhà hàng (*/ω＼*)")
                        }
                    }
                }
            }
        }
    }
}

