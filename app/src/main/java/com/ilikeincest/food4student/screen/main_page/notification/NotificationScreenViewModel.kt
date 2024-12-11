package com.ilikeincest.food4student.screen.main_page.notification

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilikeincest.food4student.model.Notification
import com.ilikeincest.food4student.service.api.UserApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import java.time.LocalDateTime
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

    fun refreshNotifications() {
        _alreadyInit.value = true
        _newNotificationAvailable.value = false
        viewModelScope.launch {
            _isRefreshing.value = true
            // TODO: connect api here
            val response = userApiService.getNotifications()
            if (response.isSuccessful) {
                response.body()?.let { notificationsList ->
                    _notifications.clear()
                    _notifications.addAll(notificationsList)
                    _isRefreshing.value = false
                } ?: run {
                    // Handle null response body if necessary
                    Log.e("NotificationViewModel", "Response body is null")
                }
            }
//            delay(1000) // mimic the api load
//            val list = List(10) {
//                Notification(
//                    id = it.toString(),
//                    image = "https://upload.wikimedia.org/wikipedia/vi/thumb/3/32/Logo_Ph%C3%BAc_Long.svg/2560px-Logo_Ph%C3%BAc_Long.svg.png",
//                    title = "Phúc Long",
//                    content = "Mời bạn tâm sự chuyện đặt món cùng ShopeeFood và nhận ngay Voucher",
//                    timestamp = Clock.System.now(),
//                    isUnread = listOf(0, 2, 3, 8).contains(it)
//                )
//            }
        }
    }

    fun markAsRead(id: String) {
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
}