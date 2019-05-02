package com.slobodanantonijevic.simpleopenweatherkt

import android.app.Activity
import android.app.Application
import com.slobodanantonijevic.simpleopenweatherkt.di.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class App : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {

        super.onCreate()

        DaggerAppComponent
            .builder()
            .application(this)
            .build()
    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity> {

        return dispatchingAndroidInjector
    }
}