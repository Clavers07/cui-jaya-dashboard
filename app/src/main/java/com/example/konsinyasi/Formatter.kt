package com.example.konsinyasi

import java.text.DecimalFormat

internal class Formatter {
    companion object {
        fun easyRead(number: Int): String {
            val formatter = DecimalFormat("#,###")  // Format dengan pemisah ribuan
            val formattedNumber = formatter.format(number)

            return formattedNumber
        }

        fun onlyInt(number: String): Int {
            val clear: String = number.replace(Regex("\\D"), "")

            return if (clear == "") 0 else clear.toInt()
        }

        fun toCurrency(number: Int, currency: String = "Rp"): String = "$currency ${easyRead(number)}"
    }
}