package com.slobodanantonijevic.simpleopenweatherkt.util

class NumberUtil {

    /**
     *
     */
    fun roundTheTemp(temp: Double): String {

        // No need for long (Math.round returns long) temperature will never be that high number
        return Integer.toString(Math.round(temp).toInt())
    }
}