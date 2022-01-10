package com.solution.nalsweather.repository

import com.solution.nalsweather.retrofit.WeatherApi

class WeatherRepository constructor(private val retrofitApi: WeatherApi) {

    suspend fun getAllWeathers() = retrofitApi.getAllWeathers()

}