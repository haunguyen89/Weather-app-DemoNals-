package com.solution.nalsweather.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.solution.nalsweather.databinding.ActivityMainBinding
import com.solution.nalsweather.retrofit.WeatherApi
import com.solution.nalsweather.repository.WeatherRepository
import com.solution.nalsweather.viewmodel.MainViewModel
import com.solution.nalsweather.viewmodel.MyViewModelFactory
import com.velmurugan.mvvmwithkotlincoroutinesandretrofit.WeatherAdapter

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: MainViewModel
    private val adapter = WeatherAdapter()
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val retrofitService = WeatherApi.getInstance()
        val mainRepository = WeatherRepository(retrofitService)
        binding.recyclerview.adapter = adapter

        viewModel = ViewModelProvider(this, MyViewModelFactory(mainRepository)).get(MainViewModel::class.java)


        viewModel.weatherList.observe(this, {
            adapter.setWeathers(it)
        })

        viewModel.errorMessage.observe(this, {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.loading.observe(this, Observer {
            if (it) {
                binding.progressDialog.visibility = View.VISIBLE
            } else {
                binding.progressDialog.visibility = View.GONE
            }
        })

        viewModel.getAllWeathers()

    }
}