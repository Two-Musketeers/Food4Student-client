package com.ilikeincest.food4student.screen.shipping.add_edit_saved_location

import android.content.Context
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilikeincest.food4student.model.Location
import com.ilikeincest.food4student.model.SavedShippingLocation
import com.ilikeincest.food4student.model.SavedShippingLocationType
import com.ilikeincest.food4student.screen.shipping.shipping_location.dataStore
import com.ilikeincest.food4student.service.AccountService
import com.ilikeincest.food4student.service.api.UserApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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
    fun getInitialData(id: String?, context: Context, defaultType: SavedShippingLocationType) {
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
                if (id.isEmpty()) {
                    // working with selected
                    val location = getCurrentLocation(context)
                    if (location != null) seedInitialFromModel(location)
                }
                else {
                    // working with saved (online)
                    val res = userApiService.getShippingAddress(id)
                    if (handleError(res)) {
                        val body = res.body()!!
                        seedInitialFromModel(body)
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

    private val LOCATION = stringPreferencesKey("location")
    private suspend fun getCurrentLocation(context: Context): SavedShippingLocation? {
        val data = context.dataStore.data.first()
        val str = data[LOCATION] ?: return null
        try {
            val location = Json.decodeFromString<SavedShippingLocation>(str)
            return location
        } catch (e: SerializationException) {
            context.dataStore.edit { it[LOCATION] = "" }
        }
        return null
    }
    private fun setCurrentLocation(location: SavedShippingLocation, context: Context) {
        viewModelScope.launch {
            context.dataStore.edit {
                it[LOCATION] = Json.encodeToString(location)
            }
        }
    }

    private fun seedInitialFromModel(body: SavedShippingLocation) {
        with(body) {
            this.name?.let { initialName = it }
            if (!this.phoneNumber.isNullOrBlank()) {
                initialPhone = this.phoneNumber
            }
            initialLocation = this.location.takeIf { it != this.address } ?: ""
            initialAddress = this.address
            initialNote = this.buildingNote ?: ""
            initialType = this.locationType.ordinal
            initialOtherLabel = this.otherLocationTypeTitle ?: ""
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

    fun saveEdited(id: String, context: Context, onSuccess: () -> Unit) {
        isLoading.value = true
        viewModelScope.launch {
            if (id.isEmpty()) {
                setCurrentLocation(buildRequestBody(id), context)
                isLoading.value = false
                onSuccess()
            }

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
        error.value = "${res.code()} ${res.message()} - ${res.errorBody()!!.string()}"
        return false
    }

    fun dismissError() {
        error.value = ""
    }
}