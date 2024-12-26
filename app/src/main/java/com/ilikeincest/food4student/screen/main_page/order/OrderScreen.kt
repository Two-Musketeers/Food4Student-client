package com.ilikeincest.food4student.screen.main_page.order

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ilikeincest.food4student.R
import com.ilikeincest.food4student.component.BetterPullToRefreshBox
import com.ilikeincest.food4student.component.LoadingDialog
import com.ilikeincest.food4student.component.preview_helper.ScreenPreview
import com.ilikeincest.food4student.dto.NoNeedToFetchAgainBuddy
import com.ilikeincest.food4student.model.OrderStatus
import com.ilikeincest.food4student.screen.main_page.order.component.OrderCard
import kotlinx.coroutines.launch

@StringRes
private fun getTabTitle(status: OrderStatus): Int = when (status) {
    OrderStatus.Pending -> R.string.order_state_pending
    OrderStatus.Approved -> R.string.order_state_delivering
    OrderStatus.Delivered -> R.string.order_state_delivered
    OrderStatus.Cancelled -> R.string.order_state_cancelled
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    onNavigateToRestaurant: ((NoNeedToFetchAgainBuddy) -> Unit)?,
    vm: OrderViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val pagerState = rememberPagerState(pageCount = { OrderStatus.entries.size })
    val coroutineScope = rememberCoroutineScope()
    val orderList = vm.orderList
    val ratings = vm.ratings

    val isLoading by vm.isLoading
    LoadingDialog(
        label = "Đang tải",
        isVisible = isLoading
    )
    LaunchedEffect(Unit) {
        vm.initData()
        vm.refreshOrders {} // silently fetch data again
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(Modifier.height(10.dp))
        PrimaryScrollableTabRow(selectedTabIndex = pagerState.currentPage) {
            OrderStatus.entries.forEachIndexed { i, status ->
                Tab(
                    selected = pagerState.currentPage == i,
                    onClick = { coroutineScope.launch {
                        pagerState.animateScrollToPage(i)
                    } },
                    text = { Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(stringResource(getTabTitle(status)))
                        val currentList = orderList[status] ?: listOf()
                        if (status !in listOf(OrderStatus.Pending, OrderStatus.Approved))
                            return@Row
                        if (currentList.isNotEmpty()) {
                            Spacer(Modifier.width(4.dp))
                            Badge {
                                Text(currentList.size.toString())
                            }
                        }
                    } },
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            beyondViewportPageCount = 2,
            verticalAlignment = Alignment.Top,
        ) { page ->
            val state = rememberLazyListState()
            var isRefreshing by remember { mutableStateOf(false) }
            BetterPullToRefreshBox(
                lazyListState = state,
                onRefresh = {
                    isRefreshing = true
                    coroutineScope.launch {
                        vm.refreshOrders {
                            isRefreshing = false
                        }
                    }
                },
                isRefreshing = isRefreshing,
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn(
                    state = state,
                    contentPadding = PaddingValues(0.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    item { Spacer(Modifier.height(0.dp)) }
                    val currentList = orderList[OrderStatus.entries[page]] ?: listOf()
                    itemsIndexed(currentList) { i, order ->
                        OrderCard(
                            id = order.id,
                            createdAt = order.createdAt,
                            restaurantName = order.restaurantName,
                            shopImageUrl = "", // TODO
                            orderItems = order.orderItems,
                            onNavigateToRestaurant = {
                                if (onNavigateToRestaurant == null) return@OrderCard
                                vm.fetchRestaurant(order.restaurantId, context, onSuccess = onNavigateToRestaurant)
                            },
                            rating = ratings.firstOrNull { it.id == order.id },
                            isReviewable = order.status == OrderStatus.Delivered,
                            onReview = { star, comment -> vm.addReview(order.id, star, comment) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        )
                        if (i != orderList.size - 1) {
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun OrderPrev() {
    ScreenPreview { OrderScreen({}) }
}