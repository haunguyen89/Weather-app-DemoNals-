package com.solution.nalsweather.local

import androidx.room.TypeConverter
import com.solution.nalsweather.model.Source

class Converters {

    @TypeConverter
    fun fromSource(source: Source): String{
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source{
        return Source(name, name)
    }
}