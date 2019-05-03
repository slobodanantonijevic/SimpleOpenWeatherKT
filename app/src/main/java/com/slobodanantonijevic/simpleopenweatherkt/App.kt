package com.slobodanantonijevic.simpleopenweatherkt

import android.app.Activity
import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.slobodanantonijevic.simpleopenweatherkt.di.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class App : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {

        super.onCreate()

        AndroidThreeTen.init(this)

        DaggerAppComponent
            .builder()
            .application(this@App)
            .build()
            .inject(this@App)
    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity> {

        return dispatchingAndroidInjector
    }
}