package com.slobodanantonijevic.simpleopenweatherkt.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.slobodanantonijevic.simpleopenweatherkt.api.OpenWeatherApi
import com.slobodanantonijevic.simpleopenweatherkt.db.CurrentWeatherDao
import com.slobodanantonijevic.simpleopenweatherkt.model.CurrentWeather
import com.slobodanantonijevic.simpleopenweatherkt.ui.CurrentWeatherViewModel
import com.slobodanantonijevic.simpleopenweatherkt.util.StreamReader
import com.slobodanantonijevic.simpleopenweatherkt.util.TestUtil
import io.reactivex.Completable
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

/**
 * Unit test for [CurrentWeatherViewModel]
 */
class CurrentWeatherViewModelTest {

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var currentWeatherDao: CurrentWeatherDao

    @Mock
    private lateinit var apiService: OpenWeatherApi

    private lateinit var viewModel: CurrentWeatherViewModel

    @Before
    fun setup() {

        MockitoAnnotations.initMocks(this)

        viewModel = CurrentWeatherViewModel(apiService, currentWeatherDao)
    }

    @Test
    fun getWeather_withNoWeatherSaved() {

        `when`(currentWeatherDao.findById(2643743)).thenReturn(Flowable.empty<CurrentWeather>())

        viewModel.currentWeather(2643743)
            .test()
            .assertNoValues()
    }

    @Test
    fun getWeather_whenWeatherIsSaved() {

        val weather = TestUtil.createCurrentWeather(
            StreamReader.readFromStream("/api-response/current-weather.json"))
        `when`(currentWeatherDao.findById(2643743)).thenReturn(Flowable.just(weather))

        viewModel.currentWeather(2643743)
            .test()
            .assertValue(weather)
    }

    @Test
    fun updateWeather_updateWeatherDateInDbViaVm() {

        val weather = TestUtil.createCurrentWeather(
            StreamReader.readFromStream("/api-response/current-weather.json"))

        currentWeatherDao.insert(weather)

        val newWeather = TestUtil.createCurrentWeather(
            StreamReader.readFromStream("/api-response/current-weather-update.json"))

        `when`(currentWeatherDao.insert(newWeather)).thenReturn(Completable.complete())

        viewModel.updateWeatherData(newWeather)
            .test()
            .assertComplete()
    }
}