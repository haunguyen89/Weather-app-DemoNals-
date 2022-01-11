package com.solution.nalsweather.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_table")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true)
    var id : Int? = null,
    val weather_state_name: String?,
    val weather_state_abbr: String?,
    val wind_direction_compass: String?,
    val created: String?,
    val applicable_date: String?,
    val min_temp: String?,
    val max_temp: String?,
    val the_temp: String?,
    val wind_speed: String?,
    val wind_direction: String?,
    val air_pressure: String?,
    val humidity: String?,
    val visibility: String?,
    val predictability: String?
)
