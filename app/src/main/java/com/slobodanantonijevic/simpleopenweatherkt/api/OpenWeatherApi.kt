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


package com.slobodanantonijevic.simpleopenweatherkt.api

import retrofit2.http.GET
import retrofit2.http.Query
import com.slobodanantonijevic.simpleopenweatherkt.model.CurrentWeather
import com.slobodanantonijevic.simpleopenweatherkt.model.Forecast
import io.reactivex.Single

interface OpenWeatherApi {

    @GET(PATH + "weather?units=metric&APPID=" + APP_ID)
    fun getCurrentWeather(
        @Query(CITY_ID) id: Int?,
        @Query(QUERY) query: String?
    ): Single<CurrentWeather>

    @GET(PATH + "forecast/daily?units=metric&APPID=" + APP_ID)
    fun getForecast(
        @Query(CITY_ID) id: Int?,
        @Query(QUERY) query: String?
    ): Single<Forecast>

    companion object {

        const val BASE_URL = "https://api.openweathermap.org/"
        const val APP_ID = "60013a62362eda7bbbd86f0e0c56a79a"

        const val CITY_ID = "id"
        const val QUERY = "q"
        const val PATH = "/data/2.5/"
    }
}