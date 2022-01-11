package com.solution.nalsweather.local

import android.content.Context
import androidx.room.Room

object DatabaseBuilder {
    private var INSTANCE: WeatherDatabase? = null

    fun getInstance(context: Context): WeatherDatabase {
        if (INSTANCE == null) {
            synchronized(WeatherDatabase::class) {
                INSTANCE = buildRoomDB(context)
            }
        }
        return INSTANCE!!
    }

    private fun buildRoomDB(context: Context) =
        Room.databaseBuilder(
            context.applicationContext,
            WeatherDatabase::class.java,
            "weather_db"
        ).build()
}