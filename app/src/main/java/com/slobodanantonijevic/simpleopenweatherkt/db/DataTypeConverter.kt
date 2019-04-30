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


package com.slobodanantonijevic.simpleopenweatherkt.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.slobodanantonijevic.simpleopenweatherkt.model.DayForecast
import com.slobodanantonijevic.simpleopenweatherkt.model.Weather
import java.lang.reflect.Type
import java.util.*

/**
 * Type Converter to instruct Room how to serialize and deserialize List(s) of data
 * Sadly we cannot use the generics here (Or at least I haven't figured out a way yet)
 */
object DataTypeConverter {

    private val gson = Gson()

    @TypeConverter
    @JvmStatic
    fun stringToWeatherList(data: String?) : List<Weather> {

        if (data == null) {

            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<Weather>>() {}.type

        return gson.fromJson(data, listType)
    }

    @TypeConverter
    @JvmStatic
    fun weatherListToString(list: List<Weather>?) : String {

        return gson.toJson(list)
    }

    @TypeConverter
    @JvmStatic
    fun stringToDayForecastList(data: String?) : List<DayForecast> {

        if (data == null) {

            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<DayForecast>>() {}.type

        return gson.fromJson(data, listType)
    }

    @TypeConverter
    @JvmStatic
    fun dayForecastListToString(list: List<DayForecast>?) : String {

        return gson.toJson(list)
    }
}