package com.solution.nalsweather.view

import android.content.Context
import android.opengl.Visibility
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : AppCompatActivity() {
    lateinit var viewModel: MainViewModel
    private val adapter = WeatherAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Companion.binding = ActivityMainBinding.inflate(layoutInflater)
        context = this.applicationContext
        setContentView(Companion.binding.root)
        val today = LocalDate.now()
        getValueDisplayUI(today)
    }


    object onItemListener : CalendarAdapter.OnItemListener {

        override fun onItemClick(position: Int, date: LocalDate?) {
            getValueDisplayUI(date)
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
        val weatherStateAbbr: HashMap<String, Int> = HashMap<String, Int>()
        private val humidityHandler = Handler()
        private val predictabilityHandler = Handler()
        private var humidity = 0
        private var predictability = 0
        private var thetemp = 0F
        private var displayDate = ""
        fun updateUI() {
            var i = 0
            binding.theTemp.text = thetemp.roundToInt().toString() + "\u2103"
            binding.dayWeatherState.text = displayDate
            Thread(Runnable {
                // this loop will run until the value of 0 becomes humidity
                while (i < humidity) {
                    i += 1
                    // Update the progress bar and display the current value
                    humidityHandler.post(Runnable {
                        binding.progressHumidity.progress = i
                        // setting current progress to the textview
                        binding.humidity.text = i.toString() + "%"
                    })
                    try {
                        Thread.sleep(20)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }).start()

            Thread(Runnable {
                // this loop will run until the value of 0 becomes predictability
                while (i < predictability) {
                    i += 1
                    // Update the progress bar and display the current value
                    predictabilityHandler.post(Runnable {
                        binding.progressPredictability.progress = i
                        // setting current progress to the textview
                        binding.predictability.text = i.toString() + "%"
                    })
                    try {
                        Thread.sleep(20)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }).start()
        }
        @RequiresApi(Build.VERSION_CODES.O)
        fun getValueDisplayUI(date: LocalDate?) {
            val formatter = DateTimeFormatter.ofPattern("EEE MMM dd, yyyy").withLocale(Locale("en", "NZ"))
            val formatterMonth = DateTimeFormatter.ofPattern("MM")
            val formatterDay = DateTimeFormatter.ofPattern("dd")
            displayDate = date!!.format(formatter)
            humidity = 0
            predictability = 0
            val retrofitService = WeatherApi.getInstance()
            GlobalScope.launch {
                val handlerVisiblePro = Handler(Looper.getMainLooper())
                handlerVisiblePro.post({
                    binding.progressDialog.visibility = View.VISIBLE
                })
                val mainRepository = WeatherRepository(retrofitService).getAllWeathers(
                    location = API_LOCATION,
                    year = date!!.year.toString(),
                    month = date!!.format(formatterMonth),
                    day = date!!.format(formatterDay)
                )
                var size = mainRepository.body()?.size
                if (size ?: 0 > 0) {
                    var i = 0
                    var humidityTemp = 0
                    var predictabilityTemp = 0
                    var thetempTemp = 0F
                    for (index in mainRepository.body()!!) {
                        humidityTemp += index.humidity.toInt()
                        predictabilityTemp += index.predictability.toInt()
                        thetempTemp += index.the_temp.toFloat()
                        i++;
                    }
                    thetemp = thetempTemp / size!!
                    humidity = humidityTemp / size!!
                    predictability = predictabilityTemp / size!!

                }
                val handlerInvisiblePro = Handler(Looper.getMainLooper())
                handlerInvisiblePro.post({
                    binding.progressDialog.visibility = View.INVISIBLE
                })
                val handler = Handler(Looper.getMainLooper())
                handler.post({
                    updateUI()
                })

            }
            CalendarUtils.selectedDate = date!!
            setWeekView()
        }
    }
}

private operator fun Any.set(weatherStateAbbr: String?, value: Int) {

}
