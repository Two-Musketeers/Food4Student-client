package com.ilikeincest.food4student.admin.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilikeincest.food4student.admin.service.ModeratorApiService
import com.ilikeincest.food4student.model.User
import com.ilikeincest.food4student.service.AccountService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val PAGE_SIZE: Int = 20

@HiltViewModel
class AdminUserViewModel @Inject constructor(
    private val apiService: ModeratorApiService,
    private val accountService: AccountService
) : ViewModel() {

    // Stores the accumulated list of users fetched from the server
    private var allUsers: MutableList<User> = mutableListOf()

    // The list of users to display, filtered based on the search query and selected role
    var users by mutableStateOf<List<User>>(emptyList())
        private set

    // The current search query
    var searchQuery by mutableStateOf("")
        private set

    // The currently selected role for filtering
    var selectedRole by mutableStateOf("All") // Default to "All"
        private set

    // Current user's role
    var currentUserRole by mutableStateOf<String?>(null)
        private set

    // Pagination variables
    private var currentPage = 1
    var isLoading by mutableStateOf(false)
        private set
    var isRefreshing by mutableStateOf(false)
        private set
    var hasMore by mutableStateOf(true)
        private set

    init {
        viewModelScope.launch {
            fetchNewUsers()
        }
        fetchCurrentUserRole()
    }

    private fun fetchCurrentUserRole() {
        viewModelScope.launch {
            try {
                currentUserRole = accountService.getUserRole()
                Log.d("AdminUserViewModel", "Current user role: $currentUserRole")
            } catch (e: Exception) {
                Log.e("AdminUserViewModel", "Error fetching current user role", e)
            }
        }
    }

    fun updateSearchQuery(newQuery: String) {
        searchQuery = newQuery
        filterUsers()
    }

    fun updateSelectedRole(role: String) {
        selectedRole = role
        filterUsers()
    }

    private fun filterUsers() {
        if (searchQuery.isEmpty()) {
            users = allUsers
        }
        val query = searchQuery.lowercase().trim()
        users = allUsers.filter { user ->
            (
                query.isEmpty() ||
                user.id.lowercase().contains(query) ||
                user.displayName?.lowercase()?.contains(query) == true ||
                user.email.lowercase().contains(query)
            ) && (
                selectedRole == "All" ||
                user.role.equals(selectedRole, ignoreCase = true)
            )
        }
    }

    fun refreshUsers() {
        // reset all metrics
        isRefreshing = true
        currentPage = 1
        isLoading = false
        hasMore = true
        viewModelScope.launch {
            fetchNewUsers(clearExisting = true)
            isRefreshing = false
        }
    }

    suspend fun fetchNewUsers(clearExisting: Boolean = false) {
        // Prevent multiple simultaneous fetches and stop if no more data
        if (isLoading || !hasMore) return

        if (!isRefreshing) isLoading = true

        try {
            val response = apiService.getUsers(currentPage, PAGE_SIZE)
            if (response.isSuccessful) {
                val fetchedUsers = response.body() ?: emptyList()
                if (fetchedUsers.size < PAGE_SIZE) {
                    // Fewer items received than requested implies no more data
                    hasMore = false
                }
                if (clearExisting) allUsers.clear()
                // Append fetched users to the accumulated list
                allUsers.addAll(fetchedUsers)
                filterUsers()
                currentPage++ // Increment page for next fetch
                Log.d("AdminUserViewModel", "Fetched users: $fetchedUsers")
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("AdminUserViewModel", "Error fetching users: $errorBody")
                Log.e("AdminUserViewModel", "HTTP status code: ${response.code()}")
                Log.e("AdminUserViewModel", "HTTP headers: ${response.headers()}")
            }
        } catch (e: Exception) {
            Log.e("AdminUserViewModel", "Exception fetching users", e)
        } finally {
            isLoading = false
        }
    }

    // Ban User
    fun banUser(userId: String) {
        viewModelScope.launch {
            try {
                val response = apiService.banUser(userId)
                if (response.isSuccessful) {
                    updateUserRoleInLocalList(userId, "Banned")
                } else {
                    Log.e("AdminUserViewModel", "Error banning user: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("AdminUserViewModel", "Exception banning user", e)
            }
        }
    }

    // Unban User
    fun unbanUser(userId: String) {
        viewModelScope.launch {
            try {
                val response = apiService.unbanUser(userId)
                if (response.isSuccessful) {
                    updateUserRoleInLocalList(userId, "User")
                } else {
                    Log.e("AdminUserViewModel", "Error unbanning user: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("AdminUserViewModel", "Exception unbanning user", e)
            }
        }
    }

    // Unban Restaurant Owner
    fun unbanRestaurantOwner(userId: String) {
        viewModelScope.launch {
            try {
                val response = apiService.unbanRestaurantOwner(userId)
                if (response.isSuccessful) {
                    updateUserRoleInLocalList(userId, "RestaurantOwner")
                } else {
                    Log.e("AdminUserViewModel", "Error unbanning restaurant owner: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("AdminUserViewModel", "Exception unbanning restaurant owner", e)
            }
        }
    }

    // Give Moderator Role
    fun giveModeratorRole(userId: String) {
        viewModelScope.launch {
            try {
                val response = apiService.giveModeratorRole(userId)
                if (response.isSuccessful) {
                    updateUserRoleInLocalList(userId, "Moderator")
                } else {
                    Log.e("AdminUserViewModel", "Error giving moderator role: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("AdminUserViewModel", "Exception giving moderator role", e)
            }
        }
    }

    // Revoke Moderator Role
    fun revokeModeratorRole(userId: String) {
        viewModelScope.launch {
            try {
                val response = apiService.revokeModeratorRole(userId)
                if (response.isSuccessful) {
                    updateUserRoleInLocalList(userId, "User")
                } else {
                    Log.e("AdminUserViewModel", "Error revoking moderator role: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("AdminUserViewModel", "Exception revoking moderator role", e)
            }
        }
    }

    // Helper method to update the user's role locally
    private fun updateUserRoleInLocalList(userId: String, newRole: String) {
        val index = allUsers.indexOfFirst { it.id == userId }
        if (index != -1) {
            allUsers = allUsers.toMutableList().apply {
                val updatedUser = this[index].copy(role = newRole)
                this[index] = updatedUser
            }
            filterUsers()
        }
    }
}