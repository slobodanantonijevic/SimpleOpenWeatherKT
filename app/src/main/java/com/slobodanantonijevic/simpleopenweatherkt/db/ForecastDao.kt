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


package com.slobodanantonijevic.simpleopenweatherkt.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.slobodanantonijevic.simpleopenweatherkt.model.Forecast
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface ForecastDao {

    @Insert(onConflict = REPLACE)
    fun insert(forecast: Forecast) : Completable

    @Query("SELECT * FROM forecast_daily WHERE id = :id")
    fun findById(id: Int) : Flowable<Forecast>
}