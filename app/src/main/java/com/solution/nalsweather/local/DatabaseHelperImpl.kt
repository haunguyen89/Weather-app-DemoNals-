package com.solution.nalsweather.local

import androidx.lifecycle.LiveData
import com.solution.nalsweather.model.WeatherEntity

class DatabaseHelperImpl(private val appDatabase: WeatherDatabase) : DatabaseHelper {
    override suspend fun getWeather(): LiveData<List<WeatherEntity>> = appDatabase.getWeatherDao().getWeather()

    override suspend fun insertAll(users: List<WeatherEntity>) = appDatabase.getWeatherDao().insertAll(users)
}