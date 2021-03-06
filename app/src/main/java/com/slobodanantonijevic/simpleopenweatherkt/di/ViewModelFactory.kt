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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
@Suppress("UNCHECKED_CAST")
class ViewModelFactory @Inject
constructor(private val viewModels: MutableMap<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        var viewModelProvider: Provider<out ViewModel>? = viewModels[modelClass]

        if (viewModelProvider == null) {

            for ((key, value) in viewModels) {

                if (modelClass.isAssignableFrom(key)) {

                    viewModelProvider = value
                    break
                }
            }
        }

        if (viewModelProvider == null) {

            throw IllegalArgumentException("unknown model class $modelClass")
        }

        try {

            return viewModelProvider.get() as T
        } catch (e: Exception) {

            throw RuntimeException(e)
        }
    }
}