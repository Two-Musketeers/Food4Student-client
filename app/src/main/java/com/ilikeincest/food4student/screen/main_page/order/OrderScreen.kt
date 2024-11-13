package com.ilikeincest.food4student.screen.main_page.order

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilikeincest.food4student.R
import com.ilikeincest.food4student.component.MonogramAvatar
import com.ilikeincest.food4student.component.OrderCard
import com.ilikeincest.food4student.component.preview_helper.ScreenPreview
import com.ilikeincest.food4student.model.OrderItem
import kotlinx.coroutines.launch
import java.time.LocalDate

// TODO: Move to model/map to model
private enum class OrderState(@StringRes val tabTitleRes: Int) {
    Pending(R.string.order_state_pending),
    Delivering(R.string.order_state_delivering),
    Delivered(R.string.order_state_delivered),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(pageCount = { OrderState.entries.size })
    val coroutineScope = rememberCoroutineScope()
    Column(modifier = modifier.fillMaxSize()) {
        PrimaryTabRow(selectedTabIndex = pagerState.currentPage) {
            OrderState.entries.forEachIndexed { i, orderState ->
                Tab(
                    selected = pagerState.currentPage == i,
                    onClick = { coroutineScope.launch {
                        pagerState.animateScrollToPage(i)
                    } },
                    text = { Text(stringResource(orderState.tabTitleRes)) },
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Top,
        ) { page ->
            LazyColumn(
                contentPadding = PaddingValues(0.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item { Spacer(Modifier.height(0.dp)) }
                // TODO: replace with actual data
                val list = List(20) { "5ea765ds$it" }
                itemsIndexed(list) { i, id ->
                    OrderCard(
                        id = id,
                        date = LocalDate.of(1969, 2, 28),
                        shopName = "Phúc Long",
                        shopId = "nuh uh",
                        shopImage = { MonogramAvatar(initials = "PL", it) },
                        orderItems = listOf(
                            OrderItem("Trà sữa Phô mai tươi", "Size S - không đá", 2, 54_000,  "https://unsplash.com/photos/IaPlDU14Oig/download?ixid=M3wxMjA3fDB8MXxhbGx8OXx8fHx8fDJ8fDE3MjY1NTQ2MDN8&force=true&w=640"),
                            OrderItem("Trà sữa Phô mai tươi 2", "Size S - không đá", 2, 54_000,  "https://unsplash.com/photos/IaPlDU14Oig/download?ixid=M3wxMjA3fDB8MXxhbGx8OXx8fHx8fDJ8fDE3MjY1NTQ2MDN8&force=true&w=640"),
                            OrderItem("Trà sữa Phô mai tươi 3", "Size S - không đá", 2, 54_000,  "https://unsplash.com/photos/IaPlDU14Oig/download?ixid=M3wxMjA3fDB8MXxhbGx8OXx8fHx8fDJ8fDE3MjY1NTQ2MDN8&force=true&w=640"),
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (i != list.size - 1) {
                        HorizontalDivider()
                    }
                }
                items(count = list.size) {
                    Text(
                        text = list[it],
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun OrderPrev() {
    ScreenPreview { OrderScreen() }
}