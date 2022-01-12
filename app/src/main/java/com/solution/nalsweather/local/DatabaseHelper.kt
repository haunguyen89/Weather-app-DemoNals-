package com.solution.nalsweather.local

import com.solution.nalsweather.model.WeatherEntity

interface DatabaseHelper {
    suspend fun getWeather(): List<WeatherEntity>

    suspend fun insertAll(weatherEntity: List<WeatherEntity>)

    suspend fun insertItem(weatherEntity: WeatherEntity)
}