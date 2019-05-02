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

package com.slobodanantonijevic.simpleopenweatherkt

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.slobodanantonijevic.simpleopenweatherkt.ui.MainActivity.Companion.FORECAST_WEATHER
import com.slobodanantonijevic.simpleopenweatherkt.util.SharedPrefManager
import retrofit2.HttpException
import javax.inject.Inject

abstract class WeatherActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPrefManager: SharedPrefManager

    lateinit var location: String
    var locationId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        location = sharedPrefManager.getSavedCityName()
        locationId = sharedPrefManager.getSavedCity()
    }

    fun handleError(error: Throwable, occurrence: Int) {

        // TODO: make some more meaningful error handling
        var message = "WEATHER: Something is not right buddy!" // default

        if (error is HttpException && occurrence != FORECAST_WEATHER) {

            val response = error as HttpException
            val code = response.code()

            when (code) {

                400, 404 ->

                    locationError(location)
                else -> {

                    message = String.format("We've got HttpException with response code: %d", code)
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }

        } else {

            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun buildLocationError(location: String): AlertDialog {

        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle(R.string.alert_title_location_error)

        val message = R.string.alert_text_location_error.toString().replace("{location}", location)
        alertDialog.setMessage(message)

        return alertDialog
    }

    fun buildTheLocationDialog(): AlertDialog {

        val builder = AlertDialog.Builder(this)

        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.alert_search_location, null)
        builder.setView(view)

        val alertDialog = builder.create()
        alertDialog.setTitle(R.string.alert_title_location_search)
        alertDialog.setMessage(R.string.alert_text_location_search.toString())

        return alertDialog
    }

    abstract fun locationError(location: String)
}