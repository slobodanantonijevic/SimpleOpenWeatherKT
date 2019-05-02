/*
 * Copyright (C) 2019 Slobodan Antonijević
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.slobodanantonijevic.simpleopenweatherkt.model

import androidx.annotation.IdRes
import com.google.gson.annotations.SerializedName
import com.slobodanantonijevic.simpleopenweatherkt.R

data class Weather(
    @SerializedName("description")
    val description: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("main")
    val main: String) {

    fun findWeatherIcon(weatherId: Int): Int {

        var icon = 0
        if (weatherId == 800) {

            icon = R.drawable.clear_day
        } else if (weatherId > 800) {

            icon = R.drawable.cloudy_day
        } else if (weatherId >= 700) {

            icon = R.drawable.mist
        } else if (weatherId >= 600) {

            icon = R.drawable.snow
        } else if (weatherId >= 511) {

            icon = R.drawable.rain_shower
        } else if (weatherId >= 500) {

            icon = R.drawable.rain
        } else if (weatherId >= 300) {

            icon = R.drawable.rain_shower
        } else if (weatherId >= 200) {

            icon = R.drawable.thunderstorm
        }

        return icon
    }

    companion object {

        // Some static config stuff mainly the markings and measurements
        const val HUMIDITY = "%"
        const val PRESSURE = "hPa"
        const val WIND = "m/s"
        const val TEMPERATURE = "°"
    }
}