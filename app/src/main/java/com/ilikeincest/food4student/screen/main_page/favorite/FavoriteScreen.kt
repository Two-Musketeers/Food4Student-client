package com.ilikeincest.food4student.screen.main_page.favorite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ilikeincest.food4student.screen.main_page.favorite.component.FavoriteCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(modifier: Modifier = Modifier) {
    val testInfo = List(20, { 0 })
    val coroutineScope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }

    PullToRefreshBox(isRefreshing, {
        coroutineScope.launch {
            isRefreshing = true
            delay(2000)
            isRefreshing = false
        }
    }) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier.padding(horizontal = 16.dp)
        ) {
            item {} // to get the default 16dp spacing as padding
            itemsIndexed(testInfo) { i, it ->
                FavoriteCard(
                    shopName = "Phúc Long",
                    starRating = "4.2",
                    distance = "2.5km",
                    timeAway = "42 phút",
                    shopImageModel = "https://images.pexels.com/photos/1525041/pexels-photo-1525041.jpeg?cs=srgb&dl=pexels-francesco-ungaro-1525041.jpg&fm=jpg",
                    isFavorite = true,
                    onFavoriteChange = {},
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
