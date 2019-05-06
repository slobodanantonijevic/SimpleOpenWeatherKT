# SimpleOpenWeatherKT
Simple Weather App that I have built due to some spare time, to keep myself in touch with Kotlin since it has been a while since I've last struck some serious code in that context. The app is pretty much a minimized hand written port of [SimpleOpenWeather](https://github.com/slobodanantonijevic/SimpleOpenWeather)

The app uses free account of [OpenWeatherMap API](https://openweathermap.org/api)

![Apache License](https://img.shields.io/badge/license-Apache--2.0-blue.svg) ![api 16+](https://img.shields.io/badge/API-16%2B-green.svg) ![Tech used](https://img.shields.io/badge/tech-ConstraintLayout%20%7C%20SVG%20%7C%20RxJava%20%7C%20Retrofit%20%7C%20Dagger%20%7C%20Butter%20Knife%20%7C%20Architecture%20Components%20%7C%20AndroidX-red.svg)

## Main technology used
- **ConstraintLayout**: A layout that allows us to create large and complex layouts with a flat view hierarchy (no nested view groups). 
- **Scalable Vector Drawables**: SVG vector graphics that allow significant reduction of the APK and app size, compared to bitmap resources.
- **Retrofit 2.0**: A type-safe HTTP client, that is used to comunicate with the designated API.
- **RxJava/RxKotlin**: A Kotlin implementation of Reactive Extensions. For composing asynchronous sequences of code, and implementing a reactive/asynchronous programming concept.
- **Dagger 2**: A dependency injection framework. We use it to implement a design pattern with minimal burden of writing the boilerplate code.
- **Architecture Components**: A collection of libraries that help us design robust, testable, and maintainable apps. Mainly used for data persistance and lifecycle awareness. (LiveData was not used, I have decided in favor of Rx Flowables, Singles and Completables)
  - **ViewModel**: Class is designed to store and manage UI-related data in a lifecycle conscious way. Used store the UI related data so it survives the device rotation
  - **Room**: A SQLite object mapping library. Used to Avoid boilerplate code and easily convert SQLite table data to Java objects.
- **AndroidX**: The open-source project that is a major improvement to the original Android Support Library.

## Important
In order for the app to work you need to provide your own **APP ID** from https://openweathermap.org, in ```api/OpenWeatherApi.java```. A free plan will work with this app. 

```
                                 Apache License
                           Version 2.0, January 2004
                        http://www.apache.org/licenses/

   Copyright 2019 Slobodan Antonijević

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
