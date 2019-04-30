package com.slobodanantonijevic.simpleopenweatherkt.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/**
 * Component to instruct Dagger what modules to look for and how to build Component instance
 */
@Singleton
@Component(modules = arrayOf(AndroidInjectionModule::class, MainActivityModule::class, AppModule::class))
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}