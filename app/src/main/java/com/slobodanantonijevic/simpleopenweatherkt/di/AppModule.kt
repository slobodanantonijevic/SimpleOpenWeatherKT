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


package com.slobodanantonijevic.simpleopenweatherkt.di

import android.app.Application
import androidx.room.Room
import com.slobodanantonijevic.simpleopenweatherkt.api.OpenWeatherApi
import com.slobodanantonijevic.simpleopenweatherkt.api.OpenWeatherApi.Companion.BASE_URL
import com.slobodanantonijevic.simpleopenweatherkt.db.CurrentWeatherDao
import com.slobodanantonijevic.simpleopenweatherkt.db.ForecastDao
import com.slobodanantonijevic.simpleopenweatherkt.db.WeatherDb
import com.slobodanantonijevic.simpleopenweatherkt.db.WeatherDb.Companion.DB_NAME
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Module to provide the Dagger all the necessary elements for our data flow
 * Such as Retrofit built client to fetch the fresh data, and Room DB and DAOs for data persistence
 */
@Module(includes = arrayOf(ViewModelModule::class))
class AppModule {

    @Singleton
    @Provides
    fun provideWeatherApi() : OpenWeatherApi {

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
            .create(OpenWeatherApi::class.java);
    }

    @Singleton
    @Provides
    fun provideDb(app: Application) : WeatherDb {

        return Room.databaseBuilder(app, WeatherDb::class.java, DB_NAME).build()
    }

    @Singleton
    @Provides
    fun provideCurrentWeatherDao(db: WeatherDb) : CurrentWeatherDao {

        return db.currentWeatherDao()
    }

    @Singleton
    @Provides
    fun provideForecastDao(db: WeatherDb) : ForecastDao {

        return db.forecastDao()
    }
}

