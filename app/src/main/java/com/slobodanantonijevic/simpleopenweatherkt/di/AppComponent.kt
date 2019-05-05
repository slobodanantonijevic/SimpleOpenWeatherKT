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
import com.slobodanantonijevic.simpleopenweatherkt.App
import com.slobodanantonijevic.simpleopenweatherkt.ui.ForecastAdapter
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/**
 * Component to instruct Dagger what modules to look for and how to build Component instance
 */
@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    MainActivityModule::class,
    AppModule::class,
    UtilsModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: App)
    fun inject(adapter: ForecastAdapter)
}