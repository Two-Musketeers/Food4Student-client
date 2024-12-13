package com.ilikeincest.food4student.screen.main_page.notification

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilikeincest.food4student.model.Notification
import com.ilikeincest.food4student.service.api.UserApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationScreenViewModel @Inject constructor(
    private val userApiService: UserApiService
) : ViewModel() {
    private val _alreadyInit = MutableStateFlow(false)
    val alreadyInit = _alreadyInit.asStateFlow()

    private val _notifications = mutableStateListOf<Notification>()
    val notifications: SnapshotStateList<Notification> = _notifications

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private val _newNotificationAvailable = MutableStateFlow(false)
    val newNotificationAvailable = _newNotificationAvailable.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    fun refreshNotifications() {
        _alreadyInit.value = true
        _newNotificationAvailable.value = false
        _isRefreshing.value = true
        viewModelScope.launch {
            val response = userApiService.getNotifications()
            if (response.isSuccessful) {
                response.body()?.let { notificationsList ->
                    _notifications.clear()
                    _notifications.addAll(notificationsList)
                    _isRefreshing.value = false
                }
            }
            else {
                showErrorDialog("Không thể tải thông báo~")
            }
        }
    }

    fun markAsRead(id: String) {
        // TODO
        val index = _notifications.indexOfFirst { it.id == id }
        if (index != -1) {
            val newValue =  _notifications[index].copy(isUnread = false)
            _notifications[index] = newValue
        }
    }

    // Do we need this?
    fun markAsUnread(id: String) {
        val index = _notifications.indexOfFirst { it.id == id }
        if (index != -1) {
            val newValue =  _notifications[index].copy(isUnread = true)
            _notifications[index] = newValue
        }
    }

    fun markAllAsRead() {
        _notifications.replaceAll {
            it.copy(isUnread = false)
        }
        _newNotificationAvailable.value = false
    }

    fun addNewNotification(it: Notification) {
        _notifications.add(0, it)
        _newNotificationAvailable.value = true
    }

    fun newNotificationAlreadySeen() {
        _newNotificationAvailable.value = false
    }

    fun showErrorDialog(message: String) {
        _errorMessage.value = message
    }
    fun dismissErrorDialog() {
        _errorMessage.value = ""
    }
}