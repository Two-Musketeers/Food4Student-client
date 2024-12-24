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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ilikeincest.food4student.R
import com.ilikeincest.food4student.component.ConfirmDiscardUnsavedDialog
import com.ilikeincest.food4student.component.DividerWithSubhead
import com.ilikeincest.food4student.component.ErrorDialog
import com.ilikeincest.food4student.component.LoadingDialog
import com.ilikeincest.food4student.model.Location
import com.ilikeincest.food4student.screen.shipping.add_edit_saved_location.component.AddEditSavedTopBar
import com.ilikeincest.food4student.screen.shipping.add_edit_saved_location.component.AddressField
import com.ilikeincest.food4student.model.SavedShippingLocationType as LocationType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditSavedLocationScreen(
    selectedAddress: Location?,
    id: String? = null,
    onNavigateUp: () -> Unit,
    onPickFromMap: () -> Unit,
    defaultType: LocationType = LocationType.Home,
    vm: AddEditSavedLocationViewModel = hiltViewModel()
) {
    val isEditScreen = id != null
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    LaunchedEffect(id, defaultType) {
        vm.getInitialData(id, defaultType)
    }

    // TODO: disable save button if missing any required field
    // TODO: enable required field's isError if field went blank
    val address by vm.address
    var location by vm.location
    var name by vm.name
    var phone by vm.phone
    var note by vm.note
    var otherLocationLabel by vm.otherLocationLabel
    var selectedLocationType by vm.selectedLocationType

    // on navigated back from pick address screen
    LaunchedEffect(selectedAddress) {
        if (selectedAddress == null) return@LaunchedEffect
        vm.setSelectedLocation(selectedAddress)
    }

    var showConfirmDiscardDialog by remember { mutableStateOf(false) }
    // TODO: add logic and data to check if we changed anything on edit screen
    // currently assume to be true
    val valueChanged by remember { derivedStateOf { vm.inputChanged() } }
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

    val error by vm.error
    if (error.isNotBlank()) {
        ErrorDialog(error, { vm.dismissError() })
    }

    val isLoading by vm.isLoading
    LoadingDialog("Đang tải...", isLoading)

    val isInputValid by remember { derivedStateOf { vm.isInputValid() } }
    Scaffold(
        topBar = { AddEditSavedTopBar(
            enableSave = isInputValid,
            isEdit = isEditScreen,
            onNavigateUp = onNavUp,
            onDelete = { id?.let { vm.delete(it, onNavigateUp) } }, // TODO: add confirm
            onSave = {
                if (id == null) {
                    vm.saveNew(onNavigateUp)
                } else {
                    vm.saveEdited(id, onNavigateUp)
                }
            },
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
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                label = { Text("Họ tên") },
                supportingText = if (name.isBlank()) { { Text("Bắt buộc") } } else null,
                isError = name.isBlank(),
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it.filter { it.isDigit() } },
                supportingText = if (phone.isBlank()) { { Text("Bắt buộc") } } else null,
                isError = phone.isBlank(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Phone
                ),
                label = { Text("Số điện thoại") },
                modifier = Modifier.fillMaxWidth()
            )

            DividerWithSubhead(
                subhead = { Text("Địa chỉ nhận") },
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
            AddressField(
                text = address,
                placeholder = "Địa chỉ",
                onClick = onPickFromMap
            )
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    supportingText = { Text("Không bắt buộc") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                    ),
                    label = { Text("Tên tòa nhà, địa điểm") },
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

            // This has by default 4dp vertical padding for some reason
            SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                LocationType.entries.forEachIndexed { i, locationType ->
                    val (icon, label) = when (locationType) {
                        LocationType.Home -> Pair(R.drawable.home, "Nhà")
                        LocationType.Work -> Pair(R.drawable.domain, "Cơ quan")
                        LocationType.Other -> Pair(R.drawable.bookmark, "Khác")
                    }
                    SegmentedButton(
                        selected = selectedLocationType == i,
                        onClick = { selectedLocationType = i },
                        shape = SegmentedButtonDefaults.itemShape(i, LocationType.entries.size),
                        icon = { Icon(painterResource(icon), null) },
                        label = { Text(label) }
                    )
                }
            }

            val otherLocation = LocationType.entries.indexOf(LocationType.Other)
            if (selectedLocationType == otherLocation) {
                OutlinedTextField(
                    value = otherLocationLabel,
                    onValueChange = { otherLocationLabel = it },
                    supportingText = { Text("Không bắt buộc") },
                    singleLine = true,
                    label = { Text("Tiêu đề địa chỉ") },
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