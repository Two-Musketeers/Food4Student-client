package com.ilikeincest.food4student.model

import androidx.compose.ui.graphics.painter.Painter
import java.time.LocalDateTime

data class Notification(
    val image: Painter, // TODO: fix this shit
    val title: String,
    val content: String,
    val timestamp: LocalDateTime,
    val isUnread: Boolean,
)
