package com.ilikeincest.food4student.screen.restaurant

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilikeincest.food4student.dto.FoodCategoryCreateDto
import com.ilikeincest.food4student.model.FoodCategory
import com.ilikeincest.food4student.model.FoodItem
import com.ilikeincest.food4student.model.Restaurant
import com.ilikeincest.food4student.model.Variation
import com.ilikeincest.food4student.model.VariationOption
import com.ilikeincest.food4student.service.api.FoodCategoryApiService
import com.ilikeincest.food4student.service.api.FoodItemApiService
import com.ilikeincest.food4student.service.api.PhotoApiService
import com.ilikeincest.food4student.service.api.RestaurantApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class RestaurantViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val restaurantApiService: RestaurantApiService,
    private val foodItemApiService: FoodItemApiService,
    private val photoApiService: PhotoApiService,
    private val foodCategoryApiService: FoodCategoryApiService
) : ViewModel() {

    private val _restaurant = MutableStateFlow<Restaurant?>(null)
    val restaurant: StateFlow<Restaurant?> = _restaurant

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    // State to track if changes are being made to a food item
    private var originalName = ""
    private var originalDescription = ""
    private var originalPrice = ""
    private var originalImageUri: Uri? = null
    private var originalVariations = emptyList<Variation>()

    // Temporary State for Unsaved Data
    private val _unsavedVariations = MutableStateFlow<List<Variation>>(emptyList())
    val unsavedVariations: StateFlow<List<Variation>> = _unsavedVariations

    private val _unsavedImageUri = MutableStateFlow<Uri?>(null)
    val unsavedImageUri: StateFlow<Uri?> = _unsavedImageUri

    private val _selectedFoodItem = MutableStateFlow<FoodItem?>(null)
    val selectedFoodItem: StateFlow<FoodItem?> = _selectedFoodItem

    private val _foodName = MutableStateFlow("")
    val foodName: StateFlow<String> = _foodName

    private val _foodDescription = MutableStateFlow("")
    val foodDescription: StateFlow<String> = _foodDescription

    private val _foodBasePrice = MutableStateFlow("")
    val foodBasePrice: StateFlow<String> = _foodBasePrice

    fun setFoodName(value: String) { _foodName.value = value }
    fun setFoodDescription(value: String?) { _foodDescription.value = value ?: "" }
    fun setFoodBasePrice(value: String) { _foodBasePrice.value = value }

    private val _selectedFoodCategory = MutableStateFlow<FoodCategory?>(null)
    val selectedFoodCategory: StateFlow<FoodCategory?> = _selectedFoodCategory

    private val _categories = MutableStateFlow<List<FoodCategory>>(emptyList())
    val categories: StateFlow<List<FoodCategory>> = _categories

    private val _isEditing = MutableStateFlow(false)

    init {
        fetchRestaurantData()
    }

    private fun fetchRestaurantData() {
        viewModelScope.launch {
            try {
                val response = restaurantApiService.getOwnedRestaurants()
                if (response.isSuccessful) {
                    response.body()?.let {
                        _restaurant.value = it
                        _categories.value = it.foodCategories
                    }
                } else {
                    showErrorDialog("${response.message()}")
                }
            } catch (e: Exception) {
                showErrorDialog(e.message.toString())
            }
        }
    }

    fun showErrorDialog(message: String) {
        _errorMessage.value = message
    }

    fun dismissErrorDialog() {
        _errorMessage.value = ""
    }

    fun addCategory(categoryName: String) {
        viewModelScope.launch {
            val createDto = FoodCategoryCreateDto(name = categoryName)
            val response = foodCategoryApiService.addFoodCategory(createDto)
            if (response.isSuccessful) {
                response.body()?.let { dto ->
                    val newCategory = FoodCategory(
                        id = dto.id,
                        name = dto.name,
                        foodItems = emptyList(),
                        restaurantId = dto.restaurantId
                    )
                    _categories.value = _categories.value + newCategory
                }
            } else {
                showErrorDialog("Failed to add category")
            }
        }
    }

    fun updateCategory(categoryId: String, newName: String) {
        viewModelScope.launch {
            val updateDto = FoodCategoryCreateDto(name = newName)
            val response = foodCategoryApiService.updateFoodCategory(categoryId, updateDto)
            if (response.isSuccessful) {
                _categories.value = _categories.value.map { category ->
                    if (category.id == categoryId) {
                        category.copy(name = newName)
                    } else {
                        category
                    }
                }
            } else {
                showErrorDialog("Failed to update category")
            }
        }
    }

    fun removeCategory(categoryId: String) {
        viewModelScope.launch {
            val response = foodCategoryApiService.deleteFoodCategory(categoryId)
            if (response.isSuccessful) {
                _categories.value = _categories.value.filter { it.id != categoryId }
            } else {
                showErrorDialog("Failed to remove category")
            }
        }
    }

    fun setSelectedFoodItem(foodItem: FoodItem?) {
        // First, set the selected item
        _selectedFoodItem.value = foodItem

        // Save the original data for comparison
        originalName = foodItem?.name ?: ""
        originalDescription = foodItem?.description ?: ""
        originalPrice = foodItem?.basePrice?.toString().orEmpty()
        originalImageUri = foodItem?.foodItemPhotoUrl?.let { Uri.parse(it) }
        originalVariations = foodItem?.variations ?: emptyList()

        // Then sync the states so they match the selected item's data
        if (foodItem != null) {
            setFoodName(foodItem.name)
            setFoodDescription(foodItem.description)
            setFoodBasePrice(foodItem.basePrice.toString())
            _unsavedVariations.value = foodItem.variations
            _unsavedImageUri.value = foodItem.foodItemPhotoUrl?.let { Uri.parse(it) }
        } else {
            // If no item is selected, clear states
            setFoodName("")
            setFoodDescription("")
            setFoodBasePrice("")
            _unsavedVariations.value = emptyList()
            _unsavedImageUri.value = null
        }

        _selectedFoodCategory.value = _categories.value.find { category ->
            category.foodItems.any { it.id == foodItem?.id }
        }
    }

    val hasUnsavedChanges: Boolean
        get() = _foodName.value != originalName ||
                _foodDescription.value != originalDescription ||
                _foodBasePrice.value != originalPrice ||
                _unsavedImageUri.value != originalImageUri ||
                _unsavedVariations.value != originalVariations

    fun addFoodCategorySelectionMode(isEditing: Boolean) {
        _isEditing.value = isEditing
    }

    fun selectFoodCategory(foodCategory: FoodCategory?) {
        foodCategory?.let { newCategory ->
            _selectedFoodCategory.value = newCategory
            if (_isEditing.value && _selectedFoodItem.value != null) {
                viewModelScope.launch {
                    val response = foodCategoryApiService.migrateFoodItem(newCategory.id, _selectedFoodItem.value!!.id)
                    if (response.isSuccessful) {
                        // Remove from old category
                        _restaurant.value = _restaurant.value?.copy(
                            foodCategories = _restaurant.value!!.foodCategories.map { category ->
                                if (category.foodItems.any { it.id == _selectedFoodItem.value!!.id }) {
                                    category.copy(
                                        foodItems = category.foodItems.filter { it.id != _selectedFoodItem.value!!.id }
                                    )
                                } else {
                                    category
                                }
                            }
                        )
                        // Add to new category
                        _restaurant.value = _restaurant.value?.copy(
                            foodCategories = _restaurant.value!!.foodCategories.map { category ->
                                if (category.id == newCategory.id) {
                                    category.copy(
                                        foodItems = category.foodItems + _selectedFoodItem.value!!
                                    )
                                } else {
                                    category
                                }
                            }
                        )
                    } else {
                        showErrorDialog("Failed to migrate food item")
                    }
                }
            }
        }
    }

    // Functions to manage unsaved variations
    fun addVariation(variation: Variation) {
        _unsavedVariations.value = _unsavedVariations.value + variation
    }

    // Functions to manage unsaved image
    fun setUnsavedImage(uri: Uri?) {
        _unsavedImageUri.value = uri
    }

    fun clearUnsavedData() {
        _unsavedVariations.value = emptyList()
        _unsavedImageUri.value = null
    }

    fun addOptionToVariation(variationIndex: Int, option: VariationOption) {
        val currentList = _unsavedVariations.value
        if (variationIndex in currentList.indices) {
            val variation = currentList[variationIndex]
            val updatedVariation = variation.copy(
                variationOptions = variation.variationOptions + option
            )
            updateVariation(variationIndex, updatedVariation)
        } else {
            Log.e("ViewModel", "addOptionToVariation: invalid index $variationIndex")
        }
    }

    fun updateVariation(index: Int, updatedVariation: Variation) {
        val currentList = _unsavedVariations.value
        if (index in currentList.indices) {
            val updatedList = currentList.toMutableList()
            updatedList[index] = updatedVariation
            _unsavedVariations.value = updatedList
        } else {
            Log.e("ViewModel", "updateVariation: invalid index $index")
        }
    }

    fun removeVariation(index: Int) {
        val currentList = _unsavedVariations.value
        if (index in currentList.indices) {
            val updatedList = currentList.toMutableList()
            updatedList.removeAt(index)
            _unsavedVariations.value = updatedList
        } else {
            Log.e("ViewModel", "removeVariation: invalid index $index")
        }
    }

    fun removeOptionFromVariation(variationIndex: Int, optionIndex: Int) {
        val currentList = _unsavedVariations.value
        if (variationIndex in currentList.indices) {
            val variation = currentList[variationIndex]
            if (optionIndex in variation.variationOptions.indices) {
                val updatedOptions = variation.variationOptions.toMutableList()
                updatedOptions.removeAt(optionIndex)
                updateVariation(variationIndex, variation.copy(variationOptions = updatedOptions))
            } else {
                Log.e("ViewModel", "removeOptionFromVariation: invalid option index $optionIndex")
            }
        } else {
            Log.e("ViewModel", "removeOptionFromVariation: invalid variation index $variationIndex")
        }
    }

    // Save Function to Handle All API Calls
    fun saveFoodItem(
        foodItemCreateDto: FoodItem,
        foodCategoryId: String
    ) {
        viewModelScope.launch {
            try {
                if (foodItemCreateDto.id.isEmpty()) {
                    // Create new food item
                    val createResponse = foodItemApiService.createFoodItem(foodItemCreateDto, foodCategoryId)
                    if (!createResponse.isSuccessful) throw Exception("Create failed")
                    val createdFoodItemDto = createResponse.body()!!

                    _unsavedImageUri.value?.let { uploadFoodItemPhoto(createdFoodItemDto.id, it) }

                    val newFoodItem = FoodItem(
                        id = createdFoodItemDto.id,
                        name = createdFoodItemDto.name,
                        description = createdFoodItemDto.description,
                        foodItemPhotoUrl = createdFoodItemDto.foodItemPhotoUrl,
                        basePrice = createdFoodItemDto.basePrice,
                        variations = createdFoodItemDto.variations.map { varDto ->
                            Variation(
                                id = varDto.id,
                                name = varDto.name,
                                minSelect = varDto.minSelect,
                                maxSelect = varDto.maxSelect,
                                variationOptions = varDto.variationOptions.map { optDto ->
                                    VariationOption(
                                        id = optDto.id,
                                        name = optDto.name,
                                        priceAdjustment = optDto.priceAdjustment
                                    )
                                }
                            )
                        }
                    )

                    _restaurant.value = _restaurant.value?.copy(
                        foodCategories = _restaurant.value!!.foodCategories.map { cat ->
                            if (cat.id == foodCategoryId) cat.copy(
                                foodItems = cat.foodItems + newFoodItem
                            ) else cat
                        }
                    )

                    _selectedFoodItem.value = newFoodItem
                    setFoodName(newFoodItem.name)
                    setFoodDescription(newFoodItem.description)
                    setFoodBasePrice(newFoodItem.basePrice.toString())

                } else {
                    val updateResponse = foodItemApiService.updateFoodItem(foodItemCreateDto.id, foodCategoryId, foodItemCreateDto)
                    val updatedFoodItemDto = updateResponse.body()!!

                    _unsavedImageUri.value?.let { uploadFoodItemPhoto(updatedFoodItemDto.id, it) }

                    val updatedFoodItem = FoodItem(
                        id = updatedFoodItemDto.id,
                        name = updatedFoodItemDto.name,
                        description = updatedFoodItemDto.description,
                        foodItemPhotoUrl = updatedFoodItemDto.foodItemPhotoUrl,
                        basePrice = updatedFoodItemDto.basePrice,
                        variations = updatedFoodItemDto.variations.map { varDto ->
                            Variation(
                                id = varDto.id,
                                name = varDto.name,
                                minSelect = varDto.minSelect,
                                maxSelect = varDto.maxSelect,
                                variationOptions = varDto.variationOptions.map { optDto ->
                                    VariationOption(
                                        id = optDto.id,
                                        name = optDto.name,
                                        priceAdjustment = optDto.priceAdjustment
                                    )
                                }
                            )
                        }
                    )

                    _restaurant.value = _restaurant.value?.copy(
                        foodCategories = _restaurant.value!!.foodCategories.map { cat ->
                            if (cat.id == foodCategoryId) cat.copy(
                                foodItems = cat.foodItems.map { item ->
                                    if (item.id == updatedFoodItem.id) updatedFoodItem else item
                                }
                            ) else cat
                        }
                    )

                    _selectedFoodItem.value = updatedFoodItem
                    setFoodName(updatedFoodItem.name)
                    setFoodDescription(updatedFoodItem.description)
                    setFoodBasePrice(updatedFoodItem.basePrice.toString())
                }

                clearUnsavedData()
            } catch (e: Exception) {
                showErrorDialog(e.message ?: "Save failed")
            }
        }
    }

    fun removeFoodItem(
        foodItemId: String,
        foodCategoryId: String
    ) {
        viewModelScope.launch {
            try {
                val response = foodItemApiService.deleteFoodItem(foodItemId, foodCategoryId)
                if (response.isSuccessful) {
                    _restaurant.value = _restaurant.value?.copy(
                        foodCategories = _restaurant.value!!.foodCategories.map { cat ->
                            if (cat.id == foodCategoryId) cat.copy(
                                foodItems = cat.foodItems.filter { it.id != foodItemId }
                            ) else cat
                        }
                    )
                } else {
                    showErrorDialog("Failed to remove food item")
                }
            } catch (e: Exception) {
                showErrorDialog(e.message.toString())
            }
        }
    }

    // Upload a photo to the server
    private suspend fun uploadFoodItemPhoto(foodItemId: String, uri: Uri) {
        try {
            val photoPart = uriToMultipartBody(uri)
            if (photoPart != null) {
                val response = photoApiService.uploadFoodItem(photoPart, foodItemId)
                if (!response.isSuccessful)
                    showErrorDialog("Photo upload failed")

            }
        } catch (e: Exception) {
            showErrorDialog(e.message.toString())
        }
    }

    // Helper functions
    private fun uriToMultipartBody(uri: Uri): MultipartBody.Part? {
        val filePath = getRealPathFromURI(uri) ?: return null
        val file = File(filePath)
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("File", file.name, requestFile)
    }

    private fun getRealPathFromURI(uri: Uri): String? {
        return uriToFile(uri)?.absolutePath
    }

    private fun uriToFile(uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val file = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}")
            val outputStream = file.outputStream()
            inputStream?.copyTo(outputStream)
            outputStream.close()
            inputStream?.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}