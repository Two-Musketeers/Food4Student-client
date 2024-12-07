package com.ilikeincest.food4student.screen.main_page.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.ilikeincest.food4student.component.preview_helper.ScreenPreview
import com.ilikeincest.food4student.screen.main_page.favorite.component.FavoriteCard
import com.ilikeincest.food4student.screen.main_page.home.component.AdsBanner
import com.ilikeincest.food4student.screen.main_page.home.component.CurrentShippingLocationCard

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToShippingLocation: () -> Unit
) {
    Column(Modifier.padding(top = 12.dp)) {
        CurrentShippingLocationCard(
            onClick = onNavigateToShippingLocation,
            currentLocation = "24 Lý Thường Kiệt, Quận 69, Tp. Thủ Đức",
            modifier = Modifier
                .padding(bottom = 12.dp)
                .padding(horizontal = 16.dp)
        )

        // TODO: move to viewmodel
        var testInfo by remember { mutableStateOf(List(20) { 0 }) }
        var selectedTab by remember { mutableIntStateOf(0) }
        val state = rememberLazyListState()
        LaunchedEffect(selectedTab) {
            // 2 is empty for example
            testInfo =
                if (selectedTab == 2) listOf()
                else List(20) { selectedTab }
            if (state.firstVisibleItemIndex > 0)
                state.animateScrollToItem(1)
        }
        LazyColumn(state = state) {
            item {
                AdsBanner(Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 12.dp)
                )
            }

            item {} // To have something to scroll up
            stickyHeader {
                // TODO: move to viewmodel
                val tabList = remember { listOf("Gần đây", "Bán chạy", "Yêu thích") }
                SecondaryTabRow(
                    selectedTabIndex = selectedTab
                ) { tabList.forEachIndexed { index, tabTitle ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(tabTitle) }
                    )
                } }
            }

            itemsIndexed(testInfo) { i, it -> Column {
                if (it == 0) FavoriteCard(
                    shopName = "Phúc Long",
                    starRating = "4.2",
                    distance = "2.5km",
                    timeAway = "42 phút",
                    shopImageModel = "https://images.pexels.com/photos/1525041/pexels-photo-1525041.jpeg?cs=srgb&dl=pexels-francesco-ungaro-1525041.jpg&fm=jpg",
                    isFavorite = true,
                    onFavoriteChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {  }
                        .padding(16.dp)
                )
                if (it == 1) FavoriteCard(
                    shopName = "Lmao",
                    starRating = "3.7",
                    distance = "2.3km",
                    timeAway = "1 phút",
                    shopImageModel = "https://onecms-res.cloudinary.com/image/upload/s--gpzlYVtf--/f_auto,q_auto/v1/mediacorp/cna/image/2022/02/24/spider_man_meme_cartoons.jpg?itok=vGHR8bav",
                    isFavorite = false,
                    onFavoriteChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {  }
                        .padding(16.dp)
                )
                HorizontalDivider()
            } }
            // TODO: add fetch more mechanism
            item {
                if (testInfo.isNotEmpty()) return@item
                Box(Modifier.fillParentMaxSize()) {
                    Text(
                        "Không có cửa hàng nào!?",
                        style = typography.titleLarge,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun Prev() { ScreenPreview {
    HomeScreen({})
} }