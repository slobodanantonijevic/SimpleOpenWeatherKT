package com.slobodanantonijevic.simpleopenweatherkt

import android.app.Activity
import android.app.Application
import android.content.Context
import com.jakewharton.threetenabp.AndroidThreeTen
import com.slobodanantonijevic.simpleopenweatherkt.di.AppComponent
import com.slobodanantonijevic.simpleopenweatherkt.di.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class App : Application(), HasActivityInjector {

    lateinit var appComponent: AppComponent

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {

        super.onCreate()

        AndroidThreeTen.init(this)

        appComponent = DaggerAppComponent
            .builder()
            .application(this@App)
            .build()
        appComponent.inject(this@App)
    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity> {

        return dispatchingAndroidInjector
    }

    companion object {

        fun from(context: Context): App {

            return context.applicationContext as App
        }
    }
}