package com.ilikeincest.food4student.model

import kotlinx.datetime.Instant

data class Notification(
    val id: String,
    val image: String?,
    val title: String,
    val content: String,
    val timestamp: Instant,
    val isUnread: Boolean,
)