package com.slobodanantonijevic.simpleopenweatherkt.util

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@Suppress("UNCHECKED_CAST")
object ObservableTestUtil {

    internal var compositeDisposable = CompositeDisposable()
    @Throws(InterruptedException::class)
    fun <T> getValue(obData: Single<T>): T {

        val data = arrayOfNulls<Any>(1)
        val latch = CountDownLatch(1)

        val disposable = obData.subscribe { currentWeather -> data[0] = currentWeather }
        compositeDisposable.add(disposable)

        latch.await(3, TimeUnit.SECONDS)
        compositeDisposable.clear()

        return data[0] as T
    }
}