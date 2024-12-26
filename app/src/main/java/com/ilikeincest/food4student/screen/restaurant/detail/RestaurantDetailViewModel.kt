package com.ilikeincest.food4student.screen.restaurant.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilikeincest.food4student.AppRoutes
import com.ilikeincest.food4student.model.FoodItem
import com.ilikeincest.food4student.model.Restaurant
import com.ilikeincest.food4student.service.api.RestaurantApiService
import com.ilikeincest.food4student.util.RestaurantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import javax.inject.Inject

@HiltViewModel
class RestaurantDetailViewModel @Inject constructor(
    private val restaurantApiService: RestaurantApiService,
    private val handle: SavedStateHandle,
    private val repository: RestaurantRepository
) : ViewModel() {
    private val _restaurantDetail = MutableStateFlow<Restaurant?>(null)
    val restaurantDetail: StateFlow<Restaurant?> = _restaurantDetail

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _showLoading = MutableStateFlow(false)
    val showLoading = _showLoading.asStateFlow()

    private val _selectedVariations = MutableStateFlow<Map<String, Map<String, List<String>>>>(emptyMap())
    val selectedVariations: StateFlow<Map<String, Map<String, List<String>>>> = _selectedVariations

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    private val _showRefreshing = MutableStateFlow(false)
    val showRefreshing = _showRefreshing.asStateFlow()
    fun refresh() {
        val restaurantId: String = handle[AppRoutes.RestaurantDetail::id.name] ?: return
        _showRefreshing.value = true
        viewModelScope.launch {
            val response = restaurantApiService.getRestaurantById(restaurantId)
            if (response.isSuccessful) {
                val restaurant = response.body()
                val distance: Double? = handle[AppRoutes.RestaurantDetail::distance.name]
                val timeAway: Int? = handle[AppRoutes.RestaurantDetail::timeAway.name]
                // Determine if the restaurant is liked based on the repository
                val isFavorite = repository.likedRestaurantIds.value.contains(restaurantId)
                _restaurantDetail.value = restaurant?.copy(
                    perStarRating = listOf(),
                    distanceInKm = distance ?: 0.0,
                    estimatedTimeInMinutes = timeAway ?: 0,
                    isFavorited = isFavorite
                )
            } else {
                _errorMessage.value = response.message()
            }
            _showRefreshing.value = false
        }
    }

    init {
        val restaurantId: String? = handle[AppRoutes.RestaurantDetail::id.name]
        if (restaurantId != null) {
            viewModelScope.launch {
                _showLoading.value = true
                val response = restaurantApiService.getRestaurantById(restaurantId)
                if (response.isSuccessful) {
                    val restaurant = response.body()
                    val distance: Double? = handle[AppRoutes.RestaurantDetail::distance.name]
                    val timeAway: Int? = handle[AppRoutes.RestaurantDetail::timeAway.name]
                    // Determine if the restaurant is liked based on the repository
                    val isFavorited = repository.likedRestaurantIds.value.contains(restaurantId)
                    _restaurantDetail.value = restaurant?.copy(
                        perStarRating = listOf(),
                        distanceInKm = distance ?: 0.0,
                        estimatedTimeInMinutes = timeAway ?: 0,
                        isFavorited = isFavorited
                    )
                } else {
                    _errorMessage.value = response.message()
                }
                _showLoading.value = false
            }

            // Observe liked restaurants and update the restaurant detail accordingly
            viewModelScope.launch {
                repository.likedRestaurantIds.collect { likedIds ->
                    _restaurantDetail.value = _restaurantDetail.value?.copy(
                        isFavorited = likedIds.contains(restaurantId)
                    )
                }
            }
        } else {
            _errorMessage.value = "Invalid restaurant ID."
            _showLoading.value = false
        }
    }

    fun decreaseCartItem(foodItem: FoodItem, variations: Map<String, List<String>>) {
        val item = _cartItems.value.find { it.foodItem.id == foodItem.id && it.selectedVariations == variations }
        if (item != null) {
            if (item.quantity <= 1) {
                // remove from cart
                _cartItems.value = _cartItems.value.filterNot { it == item }
            } else {
                // decrement quantity
                _cartItems.value = _cartItems.value.map {
                    if (it == item) it.copy(quantity = it.quantity - 1) else it
                }
            }
        }
    }

    fun isSelectedVariationsValid(foodItemId: String): Boolean {
        val restaurant = _restaurantDetail.value ?: return false
        val foodItem = restaurant.foodCategories
            .flatMap { it.foodItems }
            .find { it.id == foodItemId } ?: return false

        // Iterate over all variations
        foodItem.variations.forEach { variation ->
            if (variation.minSelect > 0) { // Only validate required variations
                val selectedOptions = selectedVariations.value[foodItemId]?.get(variation.id).orEmpty()
                val count = selectedOptions.size
                if (count < variation.minSelect) return false
                if (variation.maxSelect != null && count > variation.maxSelect) return false
            }
            // Optional variations (minSelect <= 0) are not enforced
        }
        return true
    }

    fun addToCart(item: FoodItem, variations: Map<String, List<String>>, quantity: Int) {
        // find or create CartItem
        val existingCartItem = _cartItems.value.find {
            it.foodItem.id == item.id && it.selectedVariations == variations
        }
        if (existingCartItem != null) {
            _cartItems.value = _cartItems.value.map { c ->
                if (c == existingCartItem) c.copy(quantity = c.quantity + quantity) else c
            }
        } else {
            _cartItems.value = _cartItems.value + CartItem(item, variations, quantity)
        }
        // clear after adding
        _selectedVariations.value = _selectedVariations.value.toMutableMap().apply { remove(item.id) }
    }

    fun dismissError() {
        _errorMessage.value = ""
    }

    fun selectVariation(foodItemId: String, variationId: String, optionId: String) {
        val currentMap = _selectedVariations.value.toMutableMap()
        val variationsForFood = currentMap[foodItemId]?.toMutableMap() ?: mutableMapOf()
        val currentList = (variationsForFood[variationId] ?: emptyList()).toMutableList()

        // You’ll need to look up the Variation’s maxSelect to decide radio vs multi-check
        val restaurant = _restaurantDetail.value ?: return
        val foodItem = restaurant.foodCategories
            .flatMap { it.foodItems }
            .find { it.id == foodItemId } ?: return
        val variation = foodItem.variations.find { it.id == variationId } ?: return

        if (currentList.contains(optionId)) {
            // Uncheck if allowed
            currentList.remove(optionId)
        } else {
            // If min=1 && max=1 => radio style: clear old and add new
            if (variation.minSelect == 1 && (variation.maxSelect == 1)) {
                currentList.clear()
                currentList.add(optionId)
            } else {
                // Otherwise, treat like a multi-check (respecting maxSelect)
                if (variation.maxSelect == null || currentList.size < variation.maxSelect) {
                    currentList.add(optionId)
                }
            }
        }
        variationsForFood[variationId] = currentList
        currentMap[foodItemId] = variationsForFood
        _selectedVariations.value = currentMap
    }

    val totalPrice: StateFlow<Int> = _cartItems.map { cartItems ->
        cartItems.sumOf { cartItem ->
            val itemBasePrice = cartItem.foodItem.basePrice
            // For each Variation, sum the price adjustments of all selected options
            val variationExtra = cartItem.selectedVariations.entries.sumOf { (variationId, chosenOptions) ->
                val variation = cartItem.foodItem.variations.find { it.id == variationId } ?: return@sumOf 0
                chosenOptions.sumOf { optionId ->
                    variation.variationOptions.find { it.id == optionId }?.priceAdjustment ?: 0
                }
            }
            (itemBasePrice + variationExtra) * cartItem.quantity
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0)

    // Format selected variations for display
    fun formatSelectedVariations(
        selectedVariations: Map<String, List<String>>,
        foodItem: FoodItem
    ): String {
        return selectedVariations.flatMap { (variationId, optionIds) ->
            val variation = foodItem.variations.find { it.id == variationId }
            variation?.let {
                optionIds.mapNotNull { optionId ->
                    val option = it.variationOptions.find { it.id == optionId }
                    if (option != null) "${it.name}: ${option.name}" else null
                }
            } ?: emptyList()
        }.joinToString(", ")
    }

    fun toggleLike(restaurantId: String) {
        _restaurantDetail.value = _restaurantDetail.value?.copy(
            isFavorited = !_restaurantDetail.value?.isFavorited!!
        )
        viewModelScope.launch {
            repository.toggleLike(restaurantId)
        }
    }
}

@Serializable
data class CartItem(
    val foodItem: FoodItem,
    val selectedVariations: Map<String, List<String>>,
    val quantity: Int
)
@Serializable
data class Cart(
    val restaurantId: String,
    val cartItems: List<CartItem>,
    val restaurantName: String,
    val shopImageUrl: String?
)