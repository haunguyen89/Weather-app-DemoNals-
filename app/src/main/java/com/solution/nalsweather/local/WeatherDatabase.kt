package com.solution.nalsweather.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.solution.nalsweather.model.WeatherEntity

@Database(entities = [WeatherEntity::class], version = 1)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun getWeatherDao(): WeatherDao
}