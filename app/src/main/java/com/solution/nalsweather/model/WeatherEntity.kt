package com.solution.nalsweather.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_table")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true)
    var id : Int? = null,
    var id_weather : String?,
    var weather_state_name: String?,
    var weather_state_abbr: String?,
    var wind_direction_compass: String?,
    var created: String?,
    var applicable_date: String?,
    var min_temp: String?,
    var max_temp: String?,
    var the_temp: String?,
    var wind_speed: String?,
    var wind_direction: String?,
    var air_pressure: String?,
    var humidity: String?,
    var visibility: String?,
    var predictability: String?
)
