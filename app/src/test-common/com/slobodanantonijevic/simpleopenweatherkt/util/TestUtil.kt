package com.slobodanantonijevic.simpleopenweatherkt.util

import com.google.gson.Gson
import com.slobodanantonijevic.simpleopenweatherkt.model.CurrentWeather
import com.slobodanantonijevic.simpleopenweatherkt.model.Forecast

object TestUtil {

    fun createCurrentWeather(json: String?): CurrentWeather {

        val gson = Gson()
        return gson.fromJson(json, CurrentWeather::class.java)
    }

    fun createForecast(json: String?): Forecast {

        val gson = Gson()
        return gson.fromJson(json, Forecast::class.java)
    }
}