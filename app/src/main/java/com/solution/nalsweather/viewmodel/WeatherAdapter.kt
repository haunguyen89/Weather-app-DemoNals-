package com.velmurugan.mvvmwithkotlincoroutinesandretrofit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.solution.nalsweather.databinding.WeatherLayoutBinding
import com.solution.nalsweather.model.Weather

class WeatherAdapter : RecyclerView.Adapter<MainViewHolder>() {

    var weatherList = mutableListOf<Weather>()

    fun setWeathers(weathers: List<Weather>) {
        this.weatherList = weathers.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = WeatherLayoutBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {

        val weather = weatherList[position]
        holder.binding.day.text = weather.applicable_date
        holder.binding.weatherStateName.text = weather.weather_state_name
        holder.binding.theTemp.text = weather.applicable_date
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }
}

class MainViewHolder(val binding: WeatherLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

}