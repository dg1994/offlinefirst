package com.example.offlinefirst.utils

class Helper {
    companion object {
        fun generateId(length: Int = 20): String {
            val alphaNumeric = ('a'..'z') + ('A'..'Z') + ('0'..'9')
            return alphaNumeric.shuffled().take(length).joinToString("")  //ex: bwUIoWNCSQvPZh8xaFuz
        }
    }
}