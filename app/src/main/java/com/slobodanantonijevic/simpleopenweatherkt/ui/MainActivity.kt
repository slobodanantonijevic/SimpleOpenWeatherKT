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
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.slobodanantonijevic.simpleopenweatherkt.R
import com.slobodanantonijevic.simpleopenweatherkt.WeatherActivity
import com.slobodanantonijevic.simpleopenweatherkt.model.CurrentWeather
import com.slobodanantonijevic.simpleopenweatherkt.model.DayForecast
import com.slobodanantonijevic.simpleopenweatherkt.model.Forecast
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
import java.util.ArrayList
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

    private var forecastList: List<DayForecast> = ArrayList()
    var forecastAdapter: ForecastAdapter? = null

    private var disposable = CompositeDisposable()

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AndroidInjection.inject(this@MainActivity)
        sharedPrefManager = SharedPrefManager(this@MainActivity)

        formTheRecycler()

        location = sharedPrefManager.getSavedCityName()
        locationId = sharedPrefManager.getSavedCity()

        currentWeatherViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CurrentWeatherViewModel::class.java)
        forecastViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ForecastViewModel::class.java)

        searchButton.setOnClickListener { openTheLocationDialog() }
        refreshWeather.setOnClickListener {

            getFreshCurrentWeather(locationId, null)
            getFreshForecastWeather(locationId, null)
        }
    }

    /**
     *
     */
    override fun onStart() {
        super.onStart()

        listenToCurrentWeather()
        listenToForecast()
        checkTheLocation()
    }

    /**
     *
     */
    private fun getFreshCurrentWeather(id: Int?, name: String?) {
        Animations.rotate(this@MainActivity, refreshWeather)
        refreshWeather.isEnabled = false
        searchButton.isEnabled = false
        disposable.add(currentWeatherViewModel.getFreshWeather(id, name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { currentWeather ->

                    // This (locationId == null) means we have the new city and need new disposables
                    if (locationId == null) {

                        locationId = currentWeather.id
                        location = currentWeather.name

                        sharedPrefManager.saveTheCity(locationId!!)
                    }
                    listenToCurrentWeather()
                    updateTheCurrentWeather(currentWeather)

                },
                { error -> handleError(error, CURRENT_WEATHER) }))
    }

    /**
     *
     */
    private fun getFreshForecastWeather(id: Int?, name: String?) {

        disposable.add(forecastViewModel.getFreshWeather(id, name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { forecastWeather ->

                    if (locationId == null) {

                        locationId = forecastWeather.city.id
                        location = forecastWeather.city.name

                        sharedPrefManager.saveTheCity(locationId!!)
                    }

                    forecastWeather.id = forecastWeather.city.id

                    listenToForecast()
                    updateTheForecastWeather(forecastWeather)

                },
                { error -> handleError(error, FORECAST_WEATHER) }))
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

                    if (currentWeather != null) {

                        updateTheCurrentWeatherUi(currentWeather)
                    } else {

                        openTheLocationDialog()
                    }
                },
                { error -> handleError(error, CURRENT_WEATHER) }))
    }

    /**
     *
     */
    private fun listenToForecast() {

        disposable.add(forecastViewModel.forecastWeather(locationId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { forecastWeather ->

                    if (forecastWeather != null) {

                        updateTheForecastWeatherUi(forecastWeather)
                    }
                },
                { error -> handleError(error, FORECAST_WEATHER) }
            ))
    }

    /**
     *
     */
    private fun updateTheCurrentWeather(weather: CurrentWeather) {

        disposable.add(currentWeatherViewModel.updateWeatherData(weather)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                searchButton.isEnabled = true
                refreshWeather.clearAnimation()
                refreshWeather.isEnabled = true
                },
                { error -> Log.e(TAG, "Unable to update weather", error) }))
    }

    /**
     *
     */
    private fun updateTheForecastWeather(forecast: Forecast) {

        disposable.add(forecastViewModel.updateWeatherData(forecast)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ },
                { error -> Log.e(TAG, "Unable to update forecast", error) }))
    }

    /**
     *
     */
    @SuppressLint("SetTextI18n")
    private fun updateTheCurrentWeatherUi(currentWeather: CurrentWeather) {

        currentWeather.let {

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
    private fun formTheRecycler() {

        forecastHolder.layoutManager = LinearLayoutManager(this)
        forecastAdapter = ForecastAdapter(forecastList, this@MainActivity)
        forecastHolder.adapter = forecastAdapter
    }

    /**
     *
     */
    private fun updateTheForecastWeatherUi(forecast: Forecast) {

        forecast.list.let { list ->

            forecastAdapter?.update(list)

            val controller: LayoutAnimationController
            if (ORIENTATION_PORTRAIT == resources.configuration.orientation) {

                controller = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_anim_present_from_bottom)
            } else {

                controller = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_anim_present_from_right)
            }
            forecastHolder.layoutAnimation = controller
            forecastHolder.adapter?.notifyDataSetChanged()
            forecastHolder.scheduleLayoutAnimation()
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

            val cityField = alertDialog.city
            location = cityField.text.toString()
            locationId = null
            sharedPrefManager.saveTheCity(location!!)
            sharedPrefManager.eliminateTheSavedCity()

            // We've got the new city on our hand, so we need to clear old disposables
            disposable.clear()

            getFreshCurrentWeather(locationId, location)
            getFreshForecastWeather(locationId, location)

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
