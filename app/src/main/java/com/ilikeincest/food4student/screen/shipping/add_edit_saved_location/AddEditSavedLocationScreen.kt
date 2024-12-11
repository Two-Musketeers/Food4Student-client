package com.ilikeincest.food4student.screen.shipping.add_edit_saved_location

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilikeincest.food4student.R
import com.ilikeincest.food4student.component.ConfirmDiscardUnsavedDialog
import com.ilikeincest.food4student.component.DividerWithSubhead
import com.ilikeincest.food4student.screen.shipping.add_edit_saved_location.component.AddressField
import com.ilikeincest.food4student.screen.shipping.add_edit_saved_location.component.AddEditSavedTopBar
import com.ilikeincest.food4student.model.SavedShippingLocationType as LocationType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditSavedLocationScreen(
    onNavigateUp: () -> Unit,
    id: String? = null,
) {
    val isEditScreen = id != null
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    // TODO: move to vm
    // TODO: disable save button if missing any required field
    // TODO: enable required field's isError if field went blank

    // TODO: we might need to adjust the model, or adjust this one
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    val currentAddress = ""
    var building by remember { mutableStateOf("") }
    var gate by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    var showConfirmDiscardDialog by remember { mutableStateOf(false) }
    // TODO: add logic and data to check if we changed anything on edit screen
    // currently assume to be true
    val valueChanged = true
    val onNavUp = {
        if (valueChanged) {
            showConfirmDiscardDialog = true
        } else {
            onNavigateUp()
        }
    }
    BackHandler(enabled = valueChanged) { showConfirmDiscardDialog = true }

    if (showConfirmDiscardDialog) {
        ConfirmDiscardUnsavedDialog(
            onConfirm = onNavigateUp,
            onDismiss = { showConfirmDiscardDialog = false }
        )
    }

    Scaffold(
        topBar = { AddEditSavedTopBar(
            isEdit = isEditScreen,
            onNavigateUp = onNavUp,
            onDelete = {}, // TODO
            onSave = {}, // TODO
            scrollBehavior = scrollBehavior,
        ) },
        modifier = Modifier.imePadding()
    ) { innerPadding ->
        // no need for LazyColumn
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            DividerWithSubhead(subhead = { Text("Thông tin người nhận") })
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                singleLine = true,
                label = { Text("Họ tên") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                singleLine = true,
                label = { Text("Số điện thoại") },
                modifier = Modifier.fillMaxWidth()
            )

            DividerWithSubhead(
                subhead = { Text("Địa chỉ nhận") },
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
            AddressField(
                text = currentAddress,
                placeholder = "Địa chỉ",
                onClick = {} // TODO
            )
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                OutlinedTextField(
                    value = building,
                    onValueChange = { building = it },
                    supportingText = { Text("Không bắt buộc") },
                    singleLine = true,
                    label = { Text("Tòa nhà, số tầng") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = gate,
                    onValueChange = { gate = it },
                    supportingText = { Text("Không bắt buộc") },
                    singleLine = true,
                    label = { Text("Cổng") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    supportingText = { Text("Không bắt buộc") },
                    label = { Text("Ghi chú cho tài xế") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                )
            }

            DividerWithSubhead(
                subhead = { Text("Địa chỉ nhận") },
                modifier = Modifier.padding(top = 10.dp, bottom = 4.dp)
            )

            var selectedLocation by remember { mutableIntStateOf(0) }

            // This has by default 4dp vertical padding for some reason
            SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                LocationType.entries.forEachIndexed { i, locationType ->
                    val (icon, label) = when (locationType) {
                        LocationType.Home -> Pair(R.drawable.home, "Nhà")
                        LocationType.Work -> Pair(R.drawable.domain, "Cơ quan")
                        LocationType.Other -> Pair(R.drawable.bookmark, "Khác")
                    }
                    SegmentedButton(
                        selected = selectedLocation == i,
                        onClick = { selectedLocation = i },
                        shape = SegmentedButtonDefaults.itemShape(i, LocationType.entries.size),
                        icon = { Icon(painterResource(icon), null) },
                        label = { Text(label) }
                    )
                }
            }

            val otherLocation = LocationType.entries.indexOf(LocationType.Other)
            var otherLocationLabel by remember { mutableStateOf("") }
            if (selectedLocation == otherLocation) {
                OutlinedTextField(
                    value = otherLocationLabel,
                    onValueChange = { otherLocationLabel = it },
                    supportingText = { Text("Không bắt buộc") },
                    singleLine = true,
                    label = { Text("Cổng") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            // to allow scrolling a bit up for easier one hand access
            Spacer(Modifier
                .fillMaxWidth()
                .height(200.dp)
            )
        }
    }
}

@Preview
@Composable
private fun Prev() {
    AddEditSavedLocationScreen({})
}