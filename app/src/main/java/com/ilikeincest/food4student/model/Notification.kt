package com.ilikeincest.food4student.model

import androidx.compose.ui.graphics.painter.Painter
import java.time.LocalDateTime

data class Notification(
    val id: String,
    val image: String?,
    val title: String,
    val content: String,
    val timestamp: LocalDateTime,
    val isUnread: Boolean,
)
