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


package com.slobodanantonijevic.simpleopenweatherkt.ui

import androidx.lifecycle.ViewModel
import com.slobodanantonijevic.simpleopenweatherkt.api.OpenWeatherApi
import com.slobodanantonijevic.simpleopenweatherkt.db.ForecastDao
import com.slobodanantonijevic.simpleopenweatherkt.model.Forecast
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

class ForecastViewModel @Inject constructor(
    private val api: OpenWeatherApi, private val forecastDao: ForecastDao) : ViewModel() {

    /**
     * Get the last stored weather data
     * @param cityId to fetch the last data we have stored
     *
     * @return  a [Flowable] that emits every time the data has been updated
     */
    fun forecastWeather(cityId: Int?): Flowable<Forecast> {

        return forecastDao.findById(cityId)
    }

    /**
     *
     * Search for the fresh data from the API, at least one of the params should be provided.
     * It is best practice to provide just one as providing both not null can lead to conflict on API side
     *
     * @param cityId to search for, can be null
     * @param cityName to search for, can be null
     *
     * @return a [Single] containing the fresh forecast
     */
    fun getFreshWeather(cityId: Int?, cityName: String?): Single<Forecast> {

        return api.getForecast(cityId, cityName)
    }

    /**
     * Update current weather data in the local db
     * @param weather Fresh current weather
     *
     * @return a [Completable] that completes when the new weather has been stored
     */
    fun updateWeatherData(forecast: Forecast): Completable {

        return forecastDao.insert(forecast)
    }
}