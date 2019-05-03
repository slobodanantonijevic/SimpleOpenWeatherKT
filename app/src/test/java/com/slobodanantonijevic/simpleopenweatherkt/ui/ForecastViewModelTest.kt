package com.slobodanantonijevic.simpleopenweatherkt.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.slobodanantonijevic.simpleopenweatherkt.api.OpenWeatherApi
import com.slobodanantonijevic.simpleopenweatherkt.db.ForecastDao
import com.slobodanantonijevic.simpleopenweatherkt.model.Forecast
import com.slobodanantonijevic.simpleopenweatherkt.util.StreamReader
import com.slobodanantonijevic.simpleopenweatherkt.util.TestUtil
import io.reactivex.Completable
import io.reactivex.Flowable
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

/**
 * Unit test for [ForecastViewModel]
 */
class ForecastViewModelTest {

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var forecastDao: ForecastDao

    @Mock
    private lateinit var apiService: OpenWeatherApi

    private lateinit var viewModel: ForecastViewModel

    @Before
    fun setup() {

        MockitoAnnotations.initMocks(this)

        viewModel = ForecastViewModel(apiService, forecastDao)
    }

    @Test
    fun getWeather_withNoWeatherSaved() {

        `when`(forecastDao.findById(2643743)).thenReturn(Flowable.empty<Forecast>())

        viewModel.forecastWeather(2643743)
            .test()
            .assertNoValues()
    }

    @Test
    fun getWeather_whenWeatherIsSaved() {

        val weather = TestUtil.createForecast(
            StreamReader.readFromStream("/api-response/forecast-weather.json"))
        `when`(forecastDao.findById(2643743)).thenReturn(Flowable.just(weather))

        viewModel.forecastWeather(2643743)
            .test()
            .assertValue(weather)
    }

    @Test
    fun updateWeather_updateWeatherDateInDbViaVm() {

        val weather = TestUtil.createForecast(
            StreamReader.readFromStream("/api-response/forecast-weather.json"))

        forecastDao.insert(weather)

        val newWeather = TestUtil.createForecast(
            StreamReader.readFromStream("/api-response/forecast-weather-update.json"))

        `when`(forecastDao.insert(newWeather)).thenReturn(Completable.complete())

        viewModel.updateWeatherData(newWeather)
            .test()
            .assertComplete()
    }
}