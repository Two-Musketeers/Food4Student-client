package com.ilikeincest.food4student.screen.shipping.add_edit_saved_location

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilikeincest.food4student.model.Location
import com.ilikeincest.food4student.model.SavedShippingLocation
import com.ilikeincest.food4student.model.SavedShippingLocationType
import com.ilikeincest.food4student.service.AccountService
import com.ilikeincest.food4student.service.api.UserApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AddEditSavedLocationViewModel @Inject constructor(
    private val userApiService: UserApiService,
    private val accountService: AccountService
): ViewModel() {
    private var initialName = ""
    private var initialPhone = ""
    private var initialLocation = ""
    private var initialAddress = ""
    private var initialNote = ""
    private var initialOtherLabel = ""
    private var initialType = 0

    var name = mutableStateOf("")
    var phone = mutableStateOf("")
    var location = mutableStateOf("")
    var address = mutableStateOf("")
    var note = mutableStateOf("")
    var otherLocationLabel = mutableStateOf("")
    var selectedLocationType = mutableIntStateOf(0)
    private var lat = 0.0
    private var long = 0.0

    var error = mutableStateOf("")
    var isLoading = mutableStateOf(false)

    private var isInitialized = false
    fun getInitialData(id: String?, defaultType: SavedShippingLocationType) {
        if (isInitialized) return
        isLoading.value = true
        viewModelScope.launch {
            initialType = defaultType.ordinal
            val profile = accountService.getUserProfile()
            initialName = profile.displayName
            val res = userApiService.getPhoneNumber()
            if (handleError(res))
                initialPhone = res.body()!!.string() // shouldnt be null but eh
            if (id != null) {
                val res = userApiService.getShippingAddress(id)
                if (handleError(res)) {
                    val body = res.body()!!
                    with(body) {
                        this.name?.let { initialName = it }
                        this.phoneNumber?.let { initialPhone = it }
                        initialPhone = phoneNumber ?: ""
                        initialLocation = this.location.takeIf { it != this.address } ?: ""
                        initialAddress = this.address
                        initialNote = this.buildingNote ?: ""
                        initialType = this.locationType.ordinal
                        initialOtherLabel = this.otherLocationTypeTitle ?: ""
                    }
                }
            }
            name.value = initialName
            phone.value = initialPhone
            location.value = initialLocation
            address.value = initialAddress
            note.value = initialNote
            selectedLocationType.intValue = initialType
            isInitialized = true
            isLoading.value = false
        }
    }

    fun saveNew(onSuccess: () -> Unit) {
        isLoading.value = true
        viewModelScope.launch {
            val res = userApiService.addShippingAddress(buildRequestBody(""))

            isLoading.value = false
            if (handleError(res)) {
                onSuccess()
            } else isLoading.value = false
        }
    }

    fun saveEdited(id: String, onSuccess: () -> Unit) {
        isLoading.value = true
        viewModelScope.launch {
            val res = userApiService.updateShippingAddress(id, buildRequestBody(id))

            isLoading.value = false
            if (handleError(res)) {
                onSuccess()
            } else isLoading.value = false
        }
    }

    private fun buildRequestBody(id: String) = SavedShippingLocation(
        id,
        locationType = SavedShippingLocationType.entries[selectedLocationType.intValue],
        address = address.value,
        location = location.value.ifBlank { address.value },
        name = name.value,
        phoneNumber = phone.value,
        buildingNote = note.value.ifBlank { null },
        otherLocationTypeTitle = otherLocationLabel.value.ifBlank { null },
        latitude = lat,
        longitude = long
    )

    fun delete(id: String, onSuccess: () -> Unit) {
        isLoading.value = true
        viewModelScope.launch {
            val res = userApiService.deleteShippingAddress(id)

            isLoading.value = false
            if (handleError(res)) {
                onSuccess()
            } else isLoading.value = false
        }
    }

    fun setSelectedLocation(it: Location) {
        lat = it.latitude
        long = it.longitude
        address.value = it.address
        if (it.address != it.location)
            location.value = it.location
    }

    fun inputChanged(): Boolean =
        initialName != name.value ||
        initialPhone != phone.value ||
        initialLocation != location.value ||
        initialAddress != address.value ||
        initialNote != note.value ||
        initialType != selectedLocationType.intValue ||
        (
            selectedLocationType.intValue == SavedShippingLocationType.Other.ordinal &&
            otherLocationLabel.value != initialOtherLabel
        )

    fun isInputValid(): Boolean =
        name.value.isNotBlank() &&
        phone.value.isNotBlank() &&
        address.value.isNotBlank()

    private fun <T> handleError(res: Response<T>): Boolean {
        if (res.isSuccessful) return true
        error.value = "${res.code()} ${res.message()} - ${res.errorBody()}"
        return false
    }

    fun dismissError() {
        error.value = ""
    }
}