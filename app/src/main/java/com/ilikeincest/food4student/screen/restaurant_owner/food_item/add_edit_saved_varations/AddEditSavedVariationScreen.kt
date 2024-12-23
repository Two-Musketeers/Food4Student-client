package com.ilikeincest.food4student.screen.restaurant_owner.food_item.add_edit_saved_varations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.ilikeincest.food4student.model.Variation
import com.ilikeincest.food4student.model.VariationOption
import com.ilikeincest.food4student.screen.restaurant_owner.RestaurantOwnerViewModel
import com.ilikeincest.food4student.screen.restaurant_owner.food_item.ConfirmDeleteDialog
import com.ilikeincest.food4student.screen.restaurant_owner.food_item.add_edit_saved_varations.component.VariationBottomSheet
import com.ilikeincest.food4student.screen.restaurant_owner.food_item.add_edit_saved_varations.component.VariationCard
import com.ilikeincest.food4student.screen.restaurant_owner.food_item.add_edit_saved_varations.component.VariationOptionBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditSavedVariationScreen(
    onNavigateUp: () -> Unit,
    viewModel: RestaurantOwnerViewModel
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val variations by viewModel.unsavedVariations.collectAsState()

    var showVariationBottomSheet by remember { mutableStateOf(false) }
    var showOptionBottomSheet by remember { mutableStateOf(false) }

    var isEditingVariation by remember { mutableStateOf(false) }
    var currentVariationIndex by remember { mutableIntStateOf(-1) }

    var variationName by remember { mutableStateOf("") }
    var minSelect by remember { mutableStateOf("") }
    var maxSelect by remember { mutableStateOf("") }

    var isEditingOption by remember { mutableStateOf(false) }
    var currentOptionIndex by remember { mutableIntStateOf(-1) }
    var optionName by remember { mutableStateOf("") }
    var optionPrice by remember { mutableStateOf("") }

    // Modal Bottom Sheet for adding/editing a variation
    if (showVariationBottomSheet) {
        VariationBottomSheet(
            isEditingVariation = isEditingVariation,
            variationName = variationName,
            minSelect = minSelect,
            maxSelect = maxSelect,
            onVariationNameChange = { variationName = it },
            onMinChange = { minSelect = it },
            onMaxChange = { maxSelect = it },
            onSaveVariation = {
                val min = minSelect.toIntOrNull() ?: 0
                val max = if (maxSelect.isBlank()) null else maxSelect.toIntOrNull()
                if (isEditingVariation && currentVariationIndex >= 0) {
                    val oldVariation = variations[currentVariationIndex]
                    val updatedVariation = oldVariation.copy(
                        name = variationName,
                        minSelect = min,
                        maxSelect = max
                    )
                    viewModel.updateVariation(currentVariationIndex, updatedVariation)
                } else {
                    val newVariation = Variation(
                        id = "",
                        name = variationName,
                        minSelect = min,
                        maxSelect = max,
                        variationOptions = emptyList()
                    )
                    viewModel.addVariation(newVariation)
                }
                variationName = ""
                minSelect = ""
                maxSelect = ""
                isEditingVariation = false
                showVariationBottomSheet = false
            },
            onDismissRequest = {
                variationName = ""
                minSelect = ""
                maxSelect = ""
                isEditingVariation = false
                showVariationBottomSheet = false
            }
        )
    }

    // Modal Bottom Sheet for adding/editing a variation option
    if (showOptionBottomSheet) {
        VariationOptionBottomSheet(
            isEditingOption = isEditingOption,
            optionName = optionName,
            optionPrice = optionPrice,
            onOptionNameChange = { optionName = it },
            onOptionPriceChange = { optionPrice = it },
            onSaveOption = {
                val price = optionPrice.toIntOrNull() ?: 0
                val newOption = VariationOption(
                    id = "",
                    name = optionName,
                    priceAdjustment = price
                )
                if (isEditingOption && currentOptionIndex >= 0) {
                    // Update existing option
                    val updatedOptions = variations[currentVariationIndex].variationOptions.toMutableList()
                    updatedOptions[currentOptionIndex] = newOption
                    val updatedVariation = variations[currentVariationIndex].copy(
                        variationOptions = updatedOptions
                    )
                    viewModel.updateVariation(currentVariationIndex, updatedVariation)
                } else {
                    // Add new option
                    viewModel.addOptionToVariation(currentVariationIndex, newOption)
                }
                optionName = ""
                optionPrice = ""
                isEditingOption = false
                showOptionBottomSheet = false
            },
            onDismissRequest = {
                optionName = ""
                optionPrice = ""
                isEditingOption = false
                showOptionBottomSheet = false
            }
        )
    }

    var showDeleteVariationDialog by remember { mutableStateOf(false) }
    var currentIndex by remember { mutableIntStateOf(-1) }
    if (showDeleteVariationDialog) {
        ConfirmDeleteDialog(
            title = "Xác nhận xoá",
            text = "Bạn có chắc chắn muốn xoá phân loại này và các giá trị phân loại của nó?",
            onConfirm = {
                viewModel.removeVariation(currentIndex)
                showDeleteVariationDialog = false
            },
            onDismiss = { showDeleteVariationDialog = false }
        )
    }
    var showDeleteVaritionOptionDialog by remember { mutableStateOf(false) }
    var currentVariationOptionIndex by remember { mutableIntStateOf(-1) }
    if (showDeleteVaritionOptionDialog) {
        ConfirmDeleteDialog(
            title = "Xác nhận xoá",
            text = "Bạn có chắc chắn muốn xoá giá trị phân loại này?",
            onConfirm = {
                viewModel.removeOptionFromVariation(currentVariationIndex, currentVariationOptionIndex)
                showDeleteVariationDialog = false
            },
            onDismiss = { showDeleteVaritionOptionDialog = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thêm phân loại hàng") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Trở về")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier.imePadding()
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // List of variations
            variations.forEachIndexed { index, variation ->
                VariationCard(
                    variation = variation,
                    onEditVariation = {
                        isEditingVariation = true
                        currentVariationIndex = index
                        variationName = variation.name
                        minSelect = variation.minSelect.toString()
                        maxSelect = variation.maxSelect.toString()
                        showVariationBottomSheet = true
                    },
                    onDeleteVariation = {
                        showDeleteVariationDialog = true
                        currentIndex = index
                    },
                    onAddOption = {
                        currentVariationIndex = index
                        isEditingOption = false
                        optionName = ""
                        optionPrice = ""
                        showOptionBottomSheet = true
                    },
                    onEditOption = { optIndex ->
                        isEditingOption = true
                        currentVariationIndex = index
                        currentOptionIndex = optIndex
                        optionName = variation.variationOptions[optIndex].name
                        optionPrice = variation.variationOptions[optIndex].priceAdjustment.toString()
                        showOptionBottomSheet = true
                    },
                    onDeleteOption = { optIndex ->
                        showDeleteVaritionOptionDialog = true
                        currentVariationIndex = index
                        currentOptionIndex = optIndex
                    }
                )
            }
            ElevatedCard(
                onClick = {
                    isEditingVariation = false
                    variationName = ""
                    minSelect = ""
                    maxSelect = ""
                    showVariationBottomSheet = true
                },
                colors = CardDefaults.elevatedCardColors(
                    containerColor = colorScheme.primary,
                    contentColor = colorScheme.onPrimary
                ),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddCircleOutline,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Thêm phân loại hàng")
                    }
                }
            }
        }
    }
}