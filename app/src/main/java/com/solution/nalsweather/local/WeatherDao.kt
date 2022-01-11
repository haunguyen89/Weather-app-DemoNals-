package com.solution.nalsweather.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.solution.nalsweather.model.WeatherEntity

@Dao
interface WeatherDao {

    @Query("SELECT * FROM weather_table")
    fun getWeather() : LiveData<List<WeatherEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(weatherEntity: List<WeatherEntity>)

    @Delete
    suspend fun delete(article: WeatherEntity)

    @Query("DELETE FROM weather_table")
    suspend fun deleteAllWeather()
}