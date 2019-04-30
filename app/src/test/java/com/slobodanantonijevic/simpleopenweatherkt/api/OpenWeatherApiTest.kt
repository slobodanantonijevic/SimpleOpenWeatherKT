package com.slobodanantonijevic.simpleopenweatherkt.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.slobodanantonijevic.simpleopenweatherkt.api.OpenWeatherApi.Companion.APP_ID
import com.squareup.okhttp.mockwebserver.MockResponse
import com.squareup.okhttp.mockwebserver.MockWebServer
import io.reactivex.schedulers.Schedulers
import okio.Okio
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.*


import com.slobodanantonijevic.simpleopenweatherkt.util.ObservableTestUtil.getValue
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsNull.notNullValue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat

class OpenWeatherApiTest {

    @Rule
    @JvmField // For some odd reason Kotlin reports this rule as not public. @JvmField annotation solves the problem
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var apiService: OpenWeatherApi
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun createService() {

        mockWebServer = MockWebServer()
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/").toString())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
            .create(OpenWeatherApi::class.java)
    }

    @After
    fun stopService() {

        mockWebServer.shutdown()
    }

    @Test
    @Throws(IOException::class, InterruptedException::class)
    fun getCurrentWeather() {

        enqueueResponse("current-weather.json")
        val weather = getValue(apiService.getCurrentWeather(null, "London"))

        val request = mockWebServer.takeRequest()

        assertThat<String>(request.path, `is`("/data/2.5/weather?units=metric&APPID=$APP_ID&q=London"))

        assertThat(weather, notNullValue())
        assertEquals(2643743, weather.id)
        assertEquals("London", weather.name)
    }

    @Test
    @Throws(IOException::class, InterruptedException::class)
    fun getForecast() {

        enqueueResponse("forecast-weather.json")

        val forecast = getValue(apiService.getForecast(2643743, null))
        val request = mockWebServer.takeRequest()
        assertThat<String>(request.path, `is`("/data/2.5/forecast/daily?units=metric&APPID=$APP_ID&id=2643743"))

        assertThat(forecast, notNullValue())

        val city = forecast.city
        assertEquals("London", city.name)
        assertEquals("GB", city.country)
        assertEquals(2643743, city.id)

        val daysForecast = forecast.list
        assertEquals(7, daysForecast.size)

        val day3 = daysForecast.get(2)
        assertThat(day3, notNullValue())
        assertThat(day3.pressure, `is`(1023.61))
        assertThat(day3.humidity, `is`(64.0))

        val weatherList = day3.weather
        assertThat(weatherList, notNullValue())

        val weather = weatherList.get(0)
        assertThat(weather, notNullValue())
        assertEquals("Clouds", weather.main)
    }

    /**
     * Enqueue response without headers
     *
     * @param fileName where to read the dummy data from
     */
    @Throws(IOException::class)
    private fun enqueueResponse(fileName: String) {

        enqueueResponse(fileName, Collections.emptyMap())
    }

    /**
     * Enqueue response with headers
     * We don't really need headers for this,
     * but just in case we get to need them in the future let us abstract it here
     *
     * @param fileName where to read the dummy data from
     * @param headers headers to mock
     *
     */
    @Throws(IOException::class)
    private fun enqueueResponse(fileName: String, headers: Map<String, String>) {

        val stream = javaClass.classLoader
            .getResourceAsStream("api-response/$fileName")
        val source = Okio.buffer(Okio.source(stream))
        val mockResponse = MockResponse()
        for (header: Map.Entry<String, String> in headers) {

            mockResponse.addHeader(header.key, header.value)
        }

        mockWebServer.enqueue(mockResponse
            .setBody(source.readString(StandardCharsets.UTF_8)))
    }
}