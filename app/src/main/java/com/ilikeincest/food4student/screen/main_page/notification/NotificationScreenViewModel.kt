package com.ilikeincest.food4student.screen.main_page.notification

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilikeincest.food4student.model.Notification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class NotificationScreenViewModel @Inject constructor() : ViewModel() {
    private val _alreadyInit = MutableStateFlow(false)
    val alreadyInit = _alreadyInit.asStateFlow()

    private val _notifications = mutableStateListOf<Notification>()
    val notifications: SnapshotStateList<Notification> = _notifications

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    fun refreshNotifications() {
        _alreadyInit.value = true
        viewModelScope.launch {
            _isRefreshing.value = true
            // TODO: connect api here
            delay(1000) // mimic the api load
            val list = List(10) {
                Notification(
                    id = it.toString(),
                    image = "https://upload.wikimedia.org/wikipedia/vi/thumb/3/32/Logo_Ph%C3%BAc_Long.svg/2560px-Logo_Ph%C3%BAc_Long.svg.png",
                    title = "Phúc Long",
                    content = "Mời bạn tâm sự chuyện đặt món cùng ShopeeFood và nhận ngay Voucher",
                    timestamp = LocalDateTime.now(),
                    isUnread = listOf(0, 2, 3, 8).contains(it)
                )
            }
            _notifications.clear()
            _notifications.addAll(list)
            _isRefreshing.value = false
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
    }

    fun addNewNotification(it: Notification) {
        _notifications.add(0, it)
    }
}