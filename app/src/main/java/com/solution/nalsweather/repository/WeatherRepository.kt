package com.solution.nalsweather.repository

import com.solution.nalsweather.remote.WeatherApi

class WeatherRepository constructor(private val retrofitApi: WeatherApi) {

    suspend fun getAllWeathers(location: String, year: String, month: String, day: String) = retrofitApi.getAllWeathers(location, year, month, day)

}