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

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.slobodanantonijevic.simpleopenweatherkt.R
import com.slobodanantonijevic.simpleopenweatherkt.WeatherActivity
import com.slobodanantonijevic.simpleopenweatherkt.model.CurrentWeather
import com.slobodanantonijevic.simpleopenweatherkt.model.Weather.Companion.HUMIDITY
import com.slobodanantonijevic.simpleopenweatherkt.model.Weather.Companion.PRESSURE
import com.slobodanantonijevic.simpleopenweatherkt.model.Weather.Companion.TEMPERATURE
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.template_current_weather.*
import kotlinx.android.synthetic.main.template_current_weather.city
import javax.inject.Inject

class MainActivity : WeatherActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var currentWeatherViewModel: CurrentWeatherViewModel
    private lateinit var forecastViewModel: ForecastViewModel

    private var disposable = CompositeDisposable()

    private var cityId: Int? = null
    private lateinit var cityName: String

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        currentWeatherViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CurrentWeatherViewModel::class.java)

        //TODO: [ForecastViewModel]
    }

    /**
     *
     */
    override fun onStart() {
        super.onStart()

        listenToCurrentWeather()
    }

    /**
     *
     */
    private fun listenToCurrentWeather() {

        disposable.add(currentWeatherViewModel.currentWeather(locationId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {currentWeather -> updateTheCurrentWeatherUi(currentWeather)},
                {error -> handleError(error, CURRENT_WEATHER)}))
    }

    /**
     *
     */
    private fun getFreshWeather(id: Int?, name: String?) {

        //TODO: disable the refresh and search buttons
        disposable.add(currentWeatherViewModel.getFreshWeather(id, name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { currentWeather ->

                    // This (locationId == null) means we have the new city and need new disposables
                    if (locationId == null) {

                        disposable.clear()
                        locationId = currentWeather.id
                        location = currentWeather.name

                        // Reset the listeners
                        listenToCurrentWeather()
                        // TODO: Forecast too
                    }
                    currentWeatherViewModel.updateWeatherData(currentWeather)
                },
                { error -> handleError(error, CURRENT_WEATHER) }))
    }

    /**
     *
     */
    @SuppressLint("SetTextI18n")
    private fun updateTheCurrentWeatherUi(currentWeather: CurrentWeather) {

        currentWeather.let {

            currentWeather.main.let { mainData ->

                currentTemperature.text = "${mainData.temp} $TEMPERATURE"
                pressure.text = "${mainData.pressure} $PRESSURE"
                humidity.text = "${mainData.humidity} $HUMIDITY"
                minTemp.text = "${mainData.tempMin} $TEMPERATURE"
                maxTemp.text = "${mainData.tempMax} $TEMPERATURE"
            }

            currentWeather.wind.let { windData -> wind.text = "${windData.speed} $TEMPERATURE" }

            currentWeather.weather.let { weatherList ->

                if (weatherList.count() > 0) {

                    weatherList.get(0).let { weather ->

                        val weatherImageResId = weather.findWeatherIcon(weather.id)

                        Glide.with(this)
                            .load(weatherImageResId)
                            .into(weatherIcon)
                    }
                }
            }
        }
    }

    /**
     *
     */
    override fun locationError(location: String) {

        val alertDialog = buildLocationError(location)
        alertDialog.setButton(
            AlertDialog.BUTTON_NEUTRAL, R.string.alert_button_ok.toString()) { dialog, _ ->

            openTheLocationDialog()
            dialog.dismiss()
        }
        alertDialog.show()
    }

    /**
     *
     */
    fun openTheLocationDialog() {

        val alertDialog = buildTheLocationDialog()
        alertDialog.setButton(
            AlertDialog.BUTTON_NEUTRAL, R.string.alert_button_location_search.toString()) { dialog, which ->

            val cityField = alertDialog.city
            location = cityField.text.toString()
            locationId = null
            sharedPrefManager.saveTheCity(location)
            sharedPrefManager.eliminateTheSavedCity()

            getFreshWeather(locationId, location)

            dialog.dismiss()
        }
        alertDialog.show()
    }

    /**
     *
     */
    override fun onStop() {

        super.onStop()

        // Clear all of the subscriptions
        disposable.clear()
    }

    companion object {

        const val CURRENT_WEATHER = 1
        const val FORECAST_WEATHER = 2
    }
}
