package com.solution.nalsweather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.solution.nalsweather.model.Weather
import com.solution.nalsweather.repository.WeatherRepository
import kotlinx.coroutines.*

class MainViewModel constructor(private val weatherRepository: WeatherRepository) : ViewModel() {

    val errorMessage = MutableLiveData<String>()
    val weatherList = MutableLiveData<List<Weather>>()
    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val loading = MutableLiveData<Boolean>()

    fun getAllWeathers() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = weatherRepository.getAllWeathers()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    weatherList.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }

    }

    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}