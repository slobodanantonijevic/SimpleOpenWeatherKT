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

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.slobodanantonijevic.simpleopenweatherkt.db.DataTypeConverter

@Entity
@TypeConverters(DataTypeConverter::class)
data class CurrentWeather(
    @SerializedName("base")
    val base: String,
    @Embedded(prefix = "clouds_")
    @SerializedName("clouds")
    val clouds: Clouds,
    @SerializedName("cod")
    val cod: Int,
    @SerializedName("dt")
    val dt: Int,
    @PrimaryKey
    @SerializedName("id")
    val id: Int,
    @Embedded(prefix = "main_")
    @SerializedName("main")
    val main: Main,
    @SerializedName("name")
    val name: String,
    @SerializedName("weather")
    val weather: List<Weather>,
    @Embedded(prefix = "wind_")
    @SerializedName("wind")
    val wind: Wind
)

data class Main(
    @SerializedName("humidity")
    val humidity: Double,
    @SerializedName("pressure")
    val pressure: Double,
    @SerializedName("temp")
    val temp: Double,
    @SerializedName("temp_max")
    val tempMax: Double,
    @SerializedName("temp_min")
    val tempMin: Double
)

data class Clouds(
    @SerializedName("all")
    val all: Int
)

data class Wind(
    @SerializedName("deg")
    val deg: Int,
    @SerializedName("speed")
    val speed: Double
)