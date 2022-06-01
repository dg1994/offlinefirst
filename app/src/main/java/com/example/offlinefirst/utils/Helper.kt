package com.example.offlinefirst.utils

import java.text.SimpleDateFormat
import java.util.*

class Helper {
    companion object {
        fun generateId(length: Int = 20): String {
            val alphaNumeric = ('a'..'z') + ('A'..'Z') + ('0'..'9')
            return alphaNumeric.shuffled().take(length).joinToString("")  //ex: bwUIoWNCSQvPZh8xaFuz
        }

        fun getDate(time: Long): String? {
            val formatter: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
            val dateString = formatter.format(Date(time))
            return dateString
        }
    }
}