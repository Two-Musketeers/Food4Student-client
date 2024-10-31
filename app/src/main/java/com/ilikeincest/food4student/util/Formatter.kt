package com.ilikeincest.food4student.util

import java.text.NumberFormat
import java.util.Locale

private val locale = Locale.forLanguageTag("vi-VN") // TODO: hard code for now.

fun formatPrice(price: Int): String {
    return "${formatNumber(price)}${getCurrencySymbol()}"
}

fun formatNumber(number: Int): String {
    return NumberFormat.getIntegerInstance(locale).format(number)
}

fun getCurrencySymbol(): String {
    return NumberFormat.getCurrencyInstance(locale).currency?.symbol ?: "đ"
}
