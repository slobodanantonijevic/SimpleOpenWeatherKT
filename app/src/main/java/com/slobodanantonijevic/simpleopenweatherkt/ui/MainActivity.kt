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

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.slobodanantonijevic.simpleopenweatherkt.R
import com.slobodanantonijevic.simpleopenweatherkt.WeatherActivity
import com.slobodanantonijevic.simpleopenweatherkt.model.CurrentWeather
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainActivity : WeatherActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var currentWeatherViewModel: CurrentWeatherViewModel
    private lateinit var forecastViewModel: ForecastViewModel

    private var disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        currentWeatherViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CurrentWeatherViewModel::class.java)

        //TODO: [ForecastViewModel]
    }

    override fun onStart() {
        super.onStart()

        listenToCurrentWeather()
    }

    private fun listenToCurrentWeather() {

        disposable.add(currentWeatherViewModel.currentWeather(1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {currentWeather -> updateTheCurrentWeatherUi(currentWeather)},
                {error -> handleError(error)}))
    }

    private fun updateTheCurrentWeatherUi(currentWeather: CurrentWeather) {


    }

    override fun onStop() {

        super.onStop()

        // Clear all of the subscriptions
        disposable.clear()
    }
}
