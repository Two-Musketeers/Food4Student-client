package com.ilikeincest.food4student.screen.restaurant.rating

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ilikeincest.food4student.component.ErrorDialog
import com.ilikeincest.food4student.component.preview_helper.ScreenPreview
import com.ilikeincest.food4student.dto.RatingDto
import com.ilikeincest.food4student.screen.restaurant.rating.component.OverallRatingCard
import com.ilikeincest.food4student.screen.restaurant.rating.component.RatingCard

@Composable
fun RestaurantRatingScreen(
    onNavigateUp: () -> Unit,
    vm: RestaurantRatingViewModel = hiltViewModel()
) {
    val errorMessage by vm.errorMessage
    if (errorMessage.isNotEmpty()) {
        ErrorDialog(errorMessage, onDismiss = {
            vm.dismissError()
        })
    }
    RestaurantRatingScreenContent(
        onNavigateUp,
        vm.totalRatings.collectAsState().value,
        vm.averageRating.collectAsState().value,
        vm.perStarRatings.collectAsState().value,
        vm.ratings.collectAsState().value,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RestaurantRatingScreenContent(
    onNavigateUp: () -> Unit,
    totalRatings: Int,
    averageRating: Double,
    perStarRatings: List<Int>,
    ratings: List<RatingDto>,
) {
    Scaffold(
        topBar = { TopAppBar(
            title = { Text("Review & Đánh giá") },
            navigationIcon = { IconButton(onClick = onNavigateUp) {
                Icon(Icons.AutoMirrored.Default.ArrowBack, null)
            } },
        ) }
    ) { innerPadding ->
        LazyColumn(Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
            item {
                OverallRatingCard(
                    totalRatings,
                    averageRating,
                    perStarRatings,
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(16.dp)
                        .padding(bottom = 8.dp)
                )
            }
            item {
                Text("Thực khách nói gì?",
                    style = typography.titleMedium.copy(
                        fontSize = 20.sp
                    ),
                    modifier = Modifier
//                        .clip(RoundedCornerShape(topStart = 16.dp))
//                        .clip(RoundedCornerShape(topEndPercent = 100))
                        .background(colorScheme.secondaryContainer.copy(alpha = 0.4f))
                        .fillParentMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 24.dp)
                )
            }
            itemsIndexed(ratings.sortedByDescending { it.stars }) { i, it ->
                RatingCard(it, Modifier
                    .background(colorScheme.secondaryContainer.copy(alpha = 0.4f))
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                )
                if (i != ratings.size - 1) {
                    Spacer(Modifier
                        .background(colorScheme.secondaryContainer.copy(alpha = 0.4f))
                        .height(26.dp).fillParentMaxWidth()
                    )
                }
            }
            item {
                if (ratings.isNotEmpty()) return@item
                Column(Modifier
                    .background(colorScheme.secondaryContainer.copy(alpha = 0.4f))
                    .fillParentMaxWidth()
                    .padding(horizontal = 16.dp)
                ) {
                    Text("Chưa có ai đánh giá cả... \uD83D\uDE2D")
                    Text("Nhanh tay đặt món phá tem nào!")
                }
            }
            item {
                Spacer(Modifier
//                    .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                    .background(colorScheme.secondaryContainer.copy(alpha = 0.4f))
                    .fillParentMaxWidth()
                    .height(26.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun Prev() { ScreenPreview {
    RestaurantRatingScreenContent({},
        874,
        4.3,
        listOf(2, 12, 32, 132, 696),
        listOf(
            RatingDto(
                id = "1",
                stars = 4,
                comment = "Lmao what"
            ),
            RatingDto(
                id = "2",
                stars = 5,
                comment = "Consetetur aliquyam voluptua et tempor sit. Et in aliquyam sanctus dolores tincidunt tempor invidunt nobis vel ipsum justo kasd. Mazim "
            ),
            RatingDto(
                id = "2",
                stars = 5,
                comment = "Consetetur aliquyam voluptua et tempor sit. Et in aliquyam sanctus dolores tincidunt tempor invidunt nobis vel ipsum justo kasd. Mazim "
            ),
            RatingDto(
                id = "2",
                stars = 5,
                comment = "Consetetur aliquyam voluptua et tempor sit. Et in aliquyam sanctus dolores tincidunt tempor invidunt nobis vel ipsum justo kasd. Mazim "
            ),
            RatingDto(
                id = "2",
                stars = 5,
                comment = "Consetetur aliquyam voluptua et tempor sit. Et in aliquyam sanctus dolores tincidunt tempor invidunt nobis vel ipsum justo kasd. Mazim "
            ),
        )
    )
} }

@Preview
@Composable
private fun PrevEmpty() { ScreenPreview {
    RestaurantRatingScreenContent({},
        874,
        4.3,
        listOf(2, 12, 32, 132, 696),
        listOf()
    )
} }