package com.solution.nalsweather.local

import com.solution.nalsweather.model.WeatherEntity

class DatabaseHelperImpl(private val appDatabase: WeatherDatabase) : DatabaseHelper {
    override suspend fun getWeather(): List<WeatherEntity> = appDatabase.getWeatherDao().getWeather()
    override suspend fun insertAll(weather: List<WeatherEntity>) = appDatabase.getWeatherDao().insertAll(weather)
    override suspend fun insertItem(weatherEntity: WeatherEntity) = appDatabase.getWeatherDao().insertItem(weatherEntity)
}