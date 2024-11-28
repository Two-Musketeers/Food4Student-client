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

@HiltViewModel
class AdminUserViewModel @Inject constructor(
    private val apiService: ModeratorApiService,
    private val accountService: AccountService
) : ViewModel() {

    // Stores the full list of users fetched from the server
    private var allUsers: List<User> = emptyList()

    // The list of users to display, filtered based on the search query
    var users by mutableStateOf<List<User>>(emptyList())
        private set

    // The current search query
    var searchQuery by mutableStateOf("")
        private set

    // Current user's role
    var currentUserRole by mutableStateOf<String?>(null)
        private set

    init {
        fetchUsers(pageNumber = 1, pageSize = 30)
        fetchCurrentUserRole()
    }

    private fun fetchCurrentUserRole() {
        viewModelScope.launch {
            currentUserRole = accountService.getUserRole()
            Log.d("AdminUserViewModel", "Current user role: $currentUserRole")
        }
    }

    fun updateSearchQuery(newQuery: String) {
        searchQuery = newQuery
        filterUsers()
    }

    private fun filterUsers() {
        val query = searchQuery.lowercase().trim()
        users = if (query.isEmpty()) {
            allUsers
        } else {
            allUsers.filter { user ->
                user.id.lowercase().contains(query) ||
                        (user.displayName?.lowercase()?.contains(query) ?: false) ||
                        (user.email?.lowercase()?.contains(query) ?: false)
            }
        }
    }

    fun fetchUsers(pageNumber: Int, pageSize: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.getUsers(pageNumber, pageSize)
                if (response.isSuccessful) {
                    // Store all users and apply initial filtering
                    allUsers = response.body() ?: emptyList()
                    filterUsers()
                    Log.d("AdminUserViewModel", "Fetched users: $allUsers")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("AdminUserViewModel", "Error fetching users: $errorBody")
                    Log.e("AdminUserViewModel", "HTTP status code: ${response.code()}")
                    Log.e("AdminUserViewModel", "HTTP headers: ${response.headers()}")
                }
            } catch (e: Exception) {
                Log.e("AdminUserViewModel", "Exception fetching users", e)
            }
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