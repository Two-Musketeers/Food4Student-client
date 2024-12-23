package com.ilikeincest.food4student.screen.restaurant.detail.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilikeincest.food4student.component.preview_helper.ComponentPreview
import com.ilikeincest.food4student.model.Variation
import com.ilikeincest.food4student.model.VariationOption
import com.ilikeincest.food4student.util.formatPrice
import kotlin.math.abs

private fun isValidCheckChanged(
    it: Variation,
    targetId: String,
    checkedOptionId: List<String>,
): Boolean {
    val unchecking = checkedOptionId.contains(targetId)
    if (unchecking) return true
    if (it.maxSelect == null) return true
    if (checkedOptionId.size < it.maxSelect) return true
    return false
}

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.variationSection(
    item: Variation,
    checkedOptionId: List<String>,
    onCheckedOptionChange: (targetId: String) -> Unit,
) {
    stickyHeader(key = item.id) { with(item) {
        val constraints = constraintToHumanReadable(minSelect, maxSelect)
        Text("$name ($constraints)",
            style = typography.titleMedium,
            color = colorScheme.onPrimaryContainer,
            modifier = Modifier
                .background(colorScheme.primaryContainer)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
        )
    } }
    items(item.variationOptions, key = { it.id }) { option ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable {
                    if (isValidCheckChanged(item, option.id, checkedOptionId))
                        onCheckedOptionChange(option.id)
                }
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
        ) {
            Column {
                Text(option.name)
                var formattedPrice = formatPrice(abs(option.priceAdjustment))
                if (option.priceAdjustment < 0) formattedPrice = "-$formattedPrice"
                Text(formattedPrice, color = colorScheme.outline)
            }
            Spacer(Modifier.weight(1f))
            Checkbox(
                checked = checkedOptionId.contains(option.id),
                onCheckedChange = {
                    if (isValidCheckChanged(item, option.id, checkedOptionId))
                        onCheckedOptionChange(option.id)
                }
            )
        }
    }
}

private fun constraintToHumanReadable(min: Int, max: Int?): String {
    if (max == null) {
        return "Chọn ít nhất $min"
    }
    if (min == max) {
        return "Chọn $min"
    }
    if (min == 0) {
        return "Chọn tối đa $max"
    }
    return "Chọn từ $min đến $max"
}

@Preview
@Composable
fun PrevVariation() { ComponentPreview {
    val state = remember { mutableStateListOf<List<String>>(
        listOf(),listOf(),listOf(),listOf(),listOf(),
    ) }
    LazyColumn(Modifier.width(320.dp)) { for (i in 0..4) { variationSection(
        Variation(
            id = "$i",
            name = "Size",
            minSelect = 1,
            maxSelect = 1,
            variationOptions = listOf(
                VariationOption(
                    id = "$i 123",
                    name = "Size S",
                    priceAdjustment = 0
                ),
                VariationOption(
                    id = "$i 1223",
                    name = "Size M",
                    priceAdjustment = 3000
                ),
                VariationOption(
                    id = "$i 3445",
                    name = "Size L",
                    priceAdjustment = 6000
                )
            )
        ),
        checkedOptionId = state[i],
        onCheckedOptionChange = { target->
            val checked = state[i].contains(target)
            if (checked) state[i] = state[i] - target
            else state[i] = state[i] + target
        },
    ) } }
} }
