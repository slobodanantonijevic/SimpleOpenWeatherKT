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
import android.util.Log
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
import com.slobodanantonijevic.simpleopenweatherkt.model.Weather.Companion.WIND
import com.slobodanantonijevic.simpleopenweatherkt.util.Animations
import com.slobodanantonijevic.simpleopenweatherkt.util.NumberUtil
import com.slobodanantonijevic.simpleopenweatherkt.util.SharedPrefManager
import com.slobodanantonijevic.simpleopenweatherkt.util.TimeFormatter
import dagger.android.AndroidInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.template_current_weather.*
import kotlinx.android.synthetic.main.template_current_weather.city
import javax.inject.Inject

class MainActivity : WeatherActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var timeFormatter: TimeFormatter

    @Inject
    lateinit var numUtil: NumberUtil

    private lateinit var currentWeatherViewModel: CurrentWeatherViewModel
    private lateinit var forecastViewModel: ForecastViewModel

    private var disposable = CompositeDisposable()

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AndroidInjection.inject(this@MainActivity)
        sharedPrefManager = SharedPrefManager(this@MainActivity)

        location = sharedPrefManager.getSavedCityName()
        locationId = sharedPrefManager.getSavedCity()

        currentWeatherViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CurrentWeatherViewModel::class.java)

        // TODO: [ForecastViewModel]

        searchButton.setOnClickListener { openTheLocationDialog() }
        refreshWeather.setOnClickListener { getFreshWeather(locationId, null) }

        Log.e(TAG, "Create")
    }

    /**
     *
     */
    override fun onStart() {
        super.onStart()

        listenToCurrentWeather()
        // TODO: Listen for the Forecast
        checkTheLocation()
    }

    /**
     *
     */
    private fun listenToCurrentWeather() {

        disposable.add(currentWeatherViewModel.currentWeather(locationId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { currentWeather ->
                    Log.e(TAG, "HERE I AM")
                    if (currentWeather != null) {

                        updateTheCurrentWeatherUi(currentWeather)
                    } else {

                        openTheLocationDialog()
                    } },
                { error -> handleError(error, CURRENT_WEATHER) }))
    }

    /**
     *
     */
    private fun getFreshWeather(id: Int?, name: String?) {

        //TODO: rotate refresh?
        Animations.rotate(this@MainActivity, refreshWeather)
        refreshWeather.isEnabled = false
        searchButton.isEnabled = false
        disposable.add(currentWeatherViewModel.getFreshWeather(id, name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { currentWeather ->

                    Log.e(TAG, "HERE I AM NOW")
                    Log.e(TAG, currentWeather.toString())
                    // This (locationId == null) means we have the new city and need new disposables
                    if (locationId == null) {

                        disposable.clear()
                        locationId = currentWeather.id
                        location = currentWeather.name

                        sharedPrefManager.saveTheCity(locationId!!)

                        // Reset the listeners
                        listenToCurrentWeather()
                        // TODO: Forecast too
                    }

                    updateTheCurrentWeather(currentWeather)

                },
                { error -> handleError(error, CURRENT_WEATHER) }))
    }

    /**
     *
     */
    private fun updateTheCurrentWeather(weather: CurrentWeather) {

        disposable.add(currentWeatherViewModel.updateWeatherData(weather)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                searchButton.isEnabled = true // TODO: Refresh button stop animation?
                refreshWeather.clearAnimation()
                refreshWeather.isEnabled = true
                },
                { error -> Log.e(TAG, "Unable to update username", error) }))
    }

    /**
     *
     */
    @SuppressLint("SetTextI18n")
    private fun updateTheCurrentWeatherUi(currentWeather: CurrentWeather) {

        currentWeather.let {

            // TODO: Header section too please
            city.text = currentWeather.name
            date.text = timeFormatter.headerDateTime(currentWeather.dt)

            currentWeather.main.let { mainData ->

                currentTemperature.text = "${numUtil.roundTheTemp(mainData.temp)} $TEMPERATURE"
                pressure.text = "${mainData.pressure} $PRESSURE"
                humidity.text = "${mainData.humidity} $HUMIDITY"
                minTemp.text = "${numUtil.roundTheTemp(mainData.tempMin)} $TEMPERATURE"
                maxTemp.text = "${numUtil.roundTheTemp(mainData.tempMax)} $TEMPERATURE"
            }

            currentWeather.wind.let { windData -> wind.text = "${windData.speed} $WIND" }

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
    override fun locationError(location: String?) {

        val alertDialog = buildLocationError(location)
        alertDialog.setButton(
            AlertDialog.BUTTON_NEUTRAL, getString(R.string.alert_button_ok)) { dialog, _ ->

            openTheLocationDialog()
            dialog.dismiss()
        }
        alertDialog.show()
    }

    /**
     *
     */
    private fun openTheLocationDialog() {

        val alertDialog = buildTheLocationDialog()
        alertDialog.setButton(
            AlertDialog.BUTTON_NEUTRAL, getString(R.string.alert_button_location_search)) { dialog, _ ->

            Log.e(TAG, "BOO")
            val cityField = alertDialog.city
            location = cityField.text.toString()
            locationId = null
            sharedPrefManager.saveTheCity(location!!)
            sharedPrefManager.eliminateTheSavedCity()

            getFreshWeather(locationId, location)

            dialog.dismiss()
        }
        alertDialog.show()
    }

    /**
     *
     */
    private fun checkTheLocation() {

        if (locationId == null && location == null) {

            openTheLocationDialog()
        }
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

        val TAG = MainActivity::class.java.simpleName
    }
}
