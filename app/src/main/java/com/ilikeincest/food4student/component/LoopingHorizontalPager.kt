package com.ilikeincest.food4student.component

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoopingHorizontalPager(
    itemCount: Int,
    modifier: Modifier = Modifier,
    autoScroll: Boolean = true,
    scrollDelay: Long = 6000L,
    content: @Composable (index: Int) -> Unit
) {
    // Simulate a new list with the last and first items added to simulate infinite scrolling
    val actualItemCount = itemCount + 2
    val initialPage = 1 // Start from the first actual item
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { actualItemCount }
    )

    // Infinite scrolling logic
    LaunchedEffect(pagerState.settledPage) {
        val pageCount = pagerState.pageCount
        when (pagerState.settledPage) {
            0 -> {
                // Swiped left from the first page, jump to the last item
                pagerState.scrollToPage(pageCount - 2)
            }
            pageCount - 1 -> {
                // Swiped right from the last page, jump to the first item
                pagerState.scrollToPage(1)
            }
        }
    }

    // Auto-scroll functionality
    if (autoScroll && actualItemCount > 1) {
        val coroutineScope = rememberCoroutineScope()
        LaunchedEffect(pagerState.isScrollInProgress) {
            if (pagerState.isScrollInProgress)
                return@LaunchedEffect
            while (true) {
                delay(scrollDelay)
                val nextPage = pagerState.settledPage + 1
                coroutineScope.launch {
                    pagerState.animateScrollToPage(nextPage)
                }
            }
        }
    }

    HorizontalPager(
        state = pagerState,
        pageSpacing = 16.dp,
        modifier = modifier
    ) { index ->
        val virtualIndex = when (index) {
            0 -> itemCount - 1
            itemCount + 1 -> 0
            else -> index - 1
        }
        content(virtualIndex)
    }
}