package com.solution.nalsweather.view

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.solution.nalsweather.databinding.ActivityMainBinding
import com.solution.nalsweather.remote.WeatherApi
import com.solution.nalsweather.repository.WeatherRepository
import com.solution.nalsweather.utils.API_LOCATION
import com.solution.nalsweather.view.calendar.CalendarAdapter
import com.solution.nalsweather.view.calendar.CalendarUtils
import com.solution.nalsweather.view.calendar.CalendarUtils.Companion.daysInWeekArray
import com.solution.nalsweather.viewmodel.MainViewModel
import com.solution.nalsweather.viewmodel.MyViewModelFactory
import com.velmurugan.mvvmwithkotlincoroutinesandretrofit.WeatherAdapter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: MainViewModel
    private val adapter = WeatherAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Companion.binding = ActivityMainBinding.inflate(layoutInflater)
        context = this.applicationContext
        setContentView(Companion.binding.root)

        setWeekView()
//        binding.recyclerview.adapter = adapter
//        viewModel = ViewModelProvider(this, MyViewModelFactory(mainRepository)).get(MainViewModel::class.java)


//        viewModel.weatherList.observe(this, {
//            adapter.setWeathers(it)
//        })
//
//        viewModel.errorMessage.observe(this, {
//            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
//        })
//
//        viewModel.loading.observe(this, Observer {
//            if (it) {get result from repository retrofit
//                Companion.binding.progressDialog.visibility = View.VISIBLE
//            } else {
//                Companion.binding.progressDialog.visibility = View.GONE
//            }
//        })
//
//        viewModel.getAllWeathers()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    object onItemListener : CalendarAdapter.OnItemListener {

        override fun onItemClick(position: Int, date: LocalDate?) {
            val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
            val formatterMonth = DateTimeFormatter.ofPattern("MM")
            val formatterDay = DateTimeFormatter.ofPattern("dd")
            Log.d(
                "",
                date!!.format(formatter) + "_" + date!!.year.toString() + date!!.format(
                    formatterMonth
                ) + date!!.format(formatterDay)
            )

            val retrofitService = WeatherApi.getInstance()
            GlobalScope.launch {
                val mainRepository = WeatherRepository(retrofitService).getAllWeathers(
                    location = API_LOCATION,
                    year = date!!.year.toString(),
                    month = date!!.format(formatterMonth),
                    day = date!!.format(formatterDay)
                )
                if (mainRepository.body()?.size ?: 0 > 0) {
                    var i: Int = 0
                    Log.d("Tag size", "Size" + mainRepository.body()?.size)
                    for (index in mainRepository.body()!!) {
                        Log.d("Tag size",
                            "haha day roi_" + i + "_" + mainRepository.body()
                                ?.get(i)?.weather_state_abbr
                        )
                        var count: Int = 0;
                        count = weatherStateAbbr[mainRepository.body()?.get(i)?.weather_state_abbr]!!
                        weatherStateAbbr[mainRepository.body()?.get(i)?.weather_state_abbr] = count + 1;
                        i++;
                    }
                }
            }


            CalendarUtils.selectedDate = date!!
            setWeekView()
        }

    }

    companion object {
        fun setWeekView() {
            val days: ArrayList<LocalDate> = daysInWeekArray(CalendarUtils.selectedDate)
            val calendarAdapter = CalendarAdapter(days, onItemListener)
            val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, 7)
            binding.calendarRecyclerView.setLayoutManager(layoutManager)
            binding.calendarRecyclerView.setAdapter(calendarAdapter)
        }

        lateinit var context: Context
        lateinit var binding: ActivityMainBinding
        val weatherStateAbbr:HashMap<String, Int> = HashMap<String, Int>()
    }
}

private operator fun Any.set(weatherStateAbbr: String?, value: Int) {

}
