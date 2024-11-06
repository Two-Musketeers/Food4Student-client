package com.ilikeincest.food4student.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.here.sdk.search.Place
import com.ilikeincest.food4student.R

@Composable
fun SuggestedAddressList(
    nearbyPlaces: List<Place>,
    onPlaceClick: (Place) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Địa chỉ gợi ý",
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(8.dp),
            style = typography.titleMedium
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(nearbyPlaces) { place ->
                Row(
                    modifier = Modifier
                        .clickable { onPlaceClick(place) }
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.location_on),
                        contentDescription = "LocationIcon",
                        contentScale = ContentScale.None,
                        modifier = Modifier
                            .size(24.dp) //Set the size of the image
                            .align(Alignment.Top) // Align the image to the top
                            .padding(top = 5.dp)
                    )
                    Column(modifier = Modifier.padding(start = 16.dp)) {
                        Text(
                            text = place.title,
                            style = typography.bodyLarge
                        )
                        Text(
                            text = place.address.addressText,
                            style = typography.bodyMedium
                        )
                    }
                }
                HorizontalDivider(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))
            }
        }
    }
}