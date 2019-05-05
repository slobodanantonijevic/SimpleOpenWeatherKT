package com.slobodanantonijevic.simpleopenweatherkt.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.slobodanantonijevic.simpleopenweatherkt.App
import com.slobodanantonijevic.simpleopenweatherkt.R
import com.slobodanantonijevic.simpleopenweatherkt.model.DayForecast
import com.slobodanantonijevic.simpleopenweatherkt.model.Weather.Companion.HUMIDITY
import com.slobodanantonijevic.simpleopenweatherkt.model.Weather.Companion.PRESSURE
import com.slobodanantonijevic.simpleopenweatherkt.model.Weather.Companion.WIND
import com.slobodanantonijevic.simpleopenweatherkt.util.Animations
import com.slobodanantonijevic.simpleopenweatherkt.util.NumberUtil
import com.slobodanantonijevic.simpleopenweatherkt.util.TimeFormatter
import kotlinx.android.synthetic.main.template_forecast_weather.view.*
import javax.inject.Inject

class ForecastAdapter
    constructor (var forecast: List<DayForecast>, val context: Context) :
    RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    @Inject
    lateinit var timeFormatter: TimeFormatter

    @Inject
    lateinit var numUtil: NumberUtil

    init {

        App.from(context).appComponent.inject(this)
    }

    /**
     *
     */
    fun update(freshForecast: List<DayForecast>) {

        forecast = freshForecast
    }

    /**
     *
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {

        val item = LayoutInflater.from(parent.context)
            .inflate(R.layout.template_forecast_weather, parent, false)

        return ForecastViewHolder(item)
    }

    /**
     *
     */
    override fun getItemCount(): Int {

        return forecast.size - 1
    }

    /**
     *
     */
    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {

        // We never want the first one since we have today's weather already
        val dayForecast = forecast.get(position + 1)

        holder.bindView(dayForecast)
    }

    /**
     *
     */
    inner class ForecastViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var isExpanded = false

        init {

            view.setOnClickListener{ setExpanded() }
        }

        @SuppressLint("SetTextI18n")
        fun bindView(dayForecast: DayForecast) {

            itemView.day.text = timeFormatter.weekDay(dayForecast.dt)

            // We don't really need decimal precision on a weather app
            itemView.temp.text = numUtil.roundTheTemp(dayForecast.temp.day)

            val icon = dayForecast.weather.get(0).findWeatherIcon(dayForecast.weather.get(0).id)
            Glide.with(context)
                .load(icon)
                .into(itemView.weatherIcon)

            itemView.minTemp.text = numUtil.roundTheTemp(dayForecast.temp.min)
            itemView.maxTemp.text = numUtil.roundTheTemp(dayForecast.temp.max)

            itemView.pressure.text = "${dayForecast.pressure} $PRESSURE"
            itemView.humidity.text = "${dayForecast.humidity} $HUMIDITY"
            itemView.wind.text = "${dayForecast.speed} $WIND"
        }

        /**
         *
         */
        private fun setExpanded() {

            if (isExpanded) {

                Animations.collapse(context, itemView.additionalData)
            } else {

                Animations.expand(context, itemView.additionalData)
            }
            isExpanded = !isExpanded
        }
    }
}