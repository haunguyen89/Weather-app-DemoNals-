package com.solution.nalsweather.remote

import com.solution.nalsweather.model.Weather
import com.solution.nalsweather.utils.API_URL
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherApi {

    @GET("{location}/{year}/{month}/{day}/")
    suspend fun getAllWeathers(
        @Path("location") location: String,
        @Path("year") year: String,
        @Path("month") month: String,
        @Path("day") day: String
    ) : Response<List<Weather>>

    companion object {
        var weatherApi: WeatherApi? = null
        fun getInstance() : WeatherApi {
            if (weatherApi == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                weatherApi = retrofit.create(WeatherApi::class.java)
            }
            return weatherApi!!
        }

    }
}