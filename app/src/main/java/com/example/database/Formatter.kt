package com.example.database

import java.text.DecimalFormat

internal class Formatter {
    companion object {
        fun easyRead(number: Int): String {
            val formatter = DecimalFormat("#,###")  // Format dengan pemisah ribuan
            val formattedNumber = formatter.format(number)

            return formattedNumber
        }

        fun onlyInt(number: String) = number.replace(Regex("\\D"), "").toInt()

        fun toCurrency(number: Int, currency: String = "Rp"): String {

            return "$currency ${easyRead(number)}"
        }
    }
}