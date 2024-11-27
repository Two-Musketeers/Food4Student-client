package com.ilikeincest.food4student.admin.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilikeincest.food4student.model.User
import com.ilikeincest.food4student.service.UserApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val apiService: UserApiService
) : ViewModel() {
    // Stores the full list of users fetched from the server
    private var allUsers: List<User> = emptyList()

    // The list of users to display, filtered based on the search query
    var users by mutableStateOf<List<User>>(emptyList())
        private set

    // The current search query
    var searchQuery by mutableStateOf("")
        private set

    init {
        fetchUsers(pageNumber = 1, pageSize = 30)
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
                    Log.d("UserViewModel", "Fetched users: $allUsers")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("UserViewModel", "Error fetching users: $errorBody")
                    Log.e("UserViewModel", "HTTP status code: ${response.code()}")
                    Log.e("UserViewModel", "HTTP headers: ${response.headers()}")
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Exception fetching users", e)
            }
        }
    }
    fun updateUserRole(userId: String, newRole: String) {
        viewModelScope.launch {
            try {
                val response = apiService.updateUserRole(userId, newRole)
                if (response.isSuccessful) {
                    // Update the user in the local list
                    val updatedUser = response.body()
                    if (updatedUser != null) {
                        val index = allUsers.indexOfFirst { it.id == userId }
                        if (index != -1) {
                            allUsers = allUsers.toMutableList().apply {
                                set(index, updatedUser)
                            }
                            filterUsers()
                        }
                    }
                } else {
                    Log.e("UserViewModel", "Error updating user role: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Exception updating user role", e)
            }
        }
    }
}