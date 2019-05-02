package com.slobodanantonijevic.simpleopenweatherkt.util

import android.content.Context
import android.content.SharedPreferences
import com.slobodanantonijevic.simpleopenweatherkt.R
import javax.inject.Inject

class SharedPrefManager(val context: Context) {

    @Inject
    lateinit var pref: SharedPreferences

    fun getSavedCity(): Int {

        return pref.getInt(context.getString(R.string.location_id_key), -1)
    }
    fun getSavedCityName(): String {

        return pref.getString(context.getString(R.string.location_name_key), null)!!
    }

    fun saveTheCity(cityId: Int) {

        val editor = pref.edit()
        editor.putInt(context.getString(R.string.location_id_key), cityId)
        editor.apply()
    }

    fun saveTheCity(cityName: String) {

        val editor = pref.edit()
        editor.putString(context.getString(R.string.location_name_key), cityName)
        editor.apply()
    }

    fun eliminateTheSavedCity() {

        val editor = pref.edit()
        editor.putInt(context.getString(R.string.location_id_key), -1)
        editor.apply()
    }

    companion object {

        const val BASIC_CONFIG_FILE = "basic_config_file"
    }
}