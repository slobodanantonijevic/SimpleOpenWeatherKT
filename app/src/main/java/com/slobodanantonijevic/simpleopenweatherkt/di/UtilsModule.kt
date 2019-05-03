package com.slobodanantonijevic.simpleopenweatherkt.di

import com.slobodanantonijevic.simpleopenweatherkt.util.NumberUtil
import com.slobodanantonijevic.simpleopenweatherkt.util.TimeFormatter
import dagger.Module
import dagger.Provides
import org.threeten.bp.ZoneId
import javax.inject.Singleton

/**
 * Module to provide the Dagger all the necessary elements for our utilities
 */
@Module
class UtilsModule {

    @Singleton
    @Provides
    fun provideZoneId(): ZoneId {

        return ZoneId.systemDefault()
    }

    @Singleton
    @Provides
    fun provideTimeFormatter(zoneId: ZoneId): TimeFormatter {

        return TimeFormatter(zoneId)
    }

    @Singleton
    @Provides
    fun provideNumberUtil(): NumberUtil {

        return NumberUtil()
    }
}