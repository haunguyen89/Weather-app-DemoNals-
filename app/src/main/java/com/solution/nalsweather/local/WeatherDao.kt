package com.solution.nalsweather.local

import androidx.room.*
import com.solution.nalsweather.model.WeatherEntity

@Dao
interface WeatherDao {

    @Query("SELECT * FROM weather_table")
    fun getWeather() : List<WeatherEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(weatherEntity: List<WeatherEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(weatherEntity: WeatherEntity)

    @Delete
    suspend fun delete(article: WeatherEntity)

    @Query("DELETE FROM weather_table")
    suspend fun deleteAllWeather()
}