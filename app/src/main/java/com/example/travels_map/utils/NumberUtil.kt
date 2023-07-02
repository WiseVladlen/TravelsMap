package com.example.travels_map.utils

/**
 * @param quantity number of decimal places
 */
fun Number.toString(quantity: Int): String {
    return String.format("%.${quantity}f", this).replace(',', '.')
}

fun Number.divide(number: Number): Number {
    return when (number == 0) {
        true -> 0
        false -> this.toDouble() / number.toDouble()
    }
}