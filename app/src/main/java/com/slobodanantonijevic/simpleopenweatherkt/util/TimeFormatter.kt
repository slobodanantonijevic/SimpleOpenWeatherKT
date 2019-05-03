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

package com.slobodanantonijevic.simpleopenweatherkt.util

import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Since we need the app to run on APIs prior to 26 we can't use java.time
 * but we can use ThreeTen Android Backport, which is quite efficient,
 * has been developed by Google's engineer Jake Wharton and
 * has been endorsed by Google in multiple articles
 * https://github.com/JakeWharton/ThreeTenABP
 */
@Singleton
class TimeFormatter @Inject constructor(private val zoneId: ZoneId) {

    private fun zonedDateTime(unixDateTime: Int): ZonedDateTime {

        return Instant.ofEpochMilli(unixDateTime * 1000L).atZone(zoneId)
    }

    /**
     *
     */
    fun weekDay(unixDate: Int): String {

        return DateTimeFormatter.ofPattern("EEEE").format(zonedDateTime(unixDate))
    }

    /**
     *
     */
    fun headerDateTime(unixDate: Int): String {

        return DateTimeFormatter.ofPattern("EEE, LLL dd").format(zonedDateTime(unixDate))
    }
}