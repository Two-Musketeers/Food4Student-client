package com.ilikeincest.food4student.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.here.sdk.search.Place

@Composable
fun SuggestedAddressList(
    nearbyPlaces: List<Place>,
    onPlaceClick: (Place) -> Unit
) {
    Column {
        Text(
            text = "Địa chỉ gợi ý",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            items(nearbyPlaces) { place ->
                ListItem(
                    headlineContent = { Text(text = place.title) },
                    supportingContent = { Text(text = place.address?.addressText ?: "") },
                    modifier = Modifier
                        .clickable {
                            onPlaceClick(place)
                        }
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                )
                HorizontalDivider()
            }
        }
    }
}