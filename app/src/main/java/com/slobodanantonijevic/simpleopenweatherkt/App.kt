package com.slobodanantonijevic.simpleopenweatherkt

import android.app.Activity
import android.app.Application
import android.util.Log
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class App : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {

        super.onCreate()

        Log.e("BOO", "HOO")
    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity> {

        return dispatchingAndroidInjector!!
    }
}