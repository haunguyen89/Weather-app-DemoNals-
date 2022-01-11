package com.solution.nalsweather.local

import androidx.lifecycle.LiveData
import com.solution.nalsweather.model.WeatherEntity

interface DatabaseHelper {
    suspend fun getWeather(): LiveData<List<WeatherEntity>>

    suspend fun insertAll(weatherEntity: List<WeatherEntity>)
}