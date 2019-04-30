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
import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.slobodanantonijevic.simpleopenweatherkt.db.DataTypeConverter

@Entity(tableName = "forecast_daily")
@TypeConverters(DataTypeConverter::class)
data class Forecast(
    @Embedded(prefix = "city_")
    @SerializedName("city")
    val city: City,
    @SerializedName("cnt")
    val cnt: Int,
    @SerializedName("list")
    val list: List<DayForecast>,
    @SerializedName("message")
    val message: Double,
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int
)

data class DayForecast(
    @SerializedName("clouds")
    val clouds: Int,
    @SerializedName("deg")
    val deg: Int,
    @SerializedName("dt")
    val dt: Int,
    @SerializedName("humidity")
    val humidity: Double,
    @SerializedName("pressure")
    val pressure: Double,
    @SerializedName("rain")
    val rain: Double,
    @SerializedName("speed")
    val speed: Double,
    @SerializedName("temp")
    val temp: Temp,
    @SerializedName("weather")
    val weather: List<Weather>
)

data class Temp(
    @SerializedName("day")
    val day: Double,
    @SerializedName("eve")
    val eve: Double,
    @SerializedName("max")
    val max: Double,
    @SerializedName("min")
    val min: Double,
    @SerializedName("morn")
    val morn: Double,
    @SerializedName("night")
    val night: Double
)

data class City(
    @SerializedName("country")
    val country: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("population")
    val population: Int
)