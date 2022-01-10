package com.solution.nalsweather.retrofit

import com.solution.nalsweather.model.Weather
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("1252431/2022/01/10/")
    suspend fun getAllWeathers() : Response<List<Weather>>

    @GET("/ha/api/audio_rus_app/services/api_en_test.php?")
    suspend fun doGetCateList(
        @Query("get_list_category") page: String?,
        @Query("api_key") page2: String?
    ): Call<Weather?>?

    companion object {
        var weatherApi: WeatherApi? = null
        fun getInstance() : WeatherApi {
            if (weatherApi == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://www.metaweather.com/api/location/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                weatherApi = retrofit.create(WeatherApi::class.java)
            }
            return weatherApi!!
        }

    }
}