package com.ilikeincest.food4student.util

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import nl.jacobras.humanreadable.HumanReadable
import kotlinx.datetime.format.*
import kotlinx.datetime.toLocalDateTime

private fun Int.toTimeFixed(): String {
    var result = this.toString()
    if (this > 9) result = "0$result"
    return result
}

fun Instant.timeFrom(baseInstant: Instant): String {
    val diff = this - baseInstant
    if (diff.inWholeDays > 3) {
        val datetimeInSystemZone = this.toLocalDateTime(TimeZone.currentSystemDefault())
        val format = LocalDateTime.Format {
            dayOfMonth()
            char('/')
            monthNumber()
            char('/')
            year()
            char(' ')
            hour()
            char(':')
            minute()
        }
        return format.format(datetimeInSystemZone)
    }
    if (diff.inWholeSeconds < 60) {
        return HumanReadable.timeAgo(Instant.DISTANT_PAST, Instant.DISTANT_PAST)
    }
    return HumanReadable.timeAgo(this, baseInstant)
}