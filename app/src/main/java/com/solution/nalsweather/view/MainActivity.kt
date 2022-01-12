package com.solution.nalsweather.view

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.solution.nalsweather.R
import com.solution.nalsweather.databinding.ActivityMainBinding
import com.solution.nalsweather.local.DatabaseBuilder
import com.solution.nalsweather.local.DatabaseHelperImpl
import com.solution.nalsweather.model.WeatherEntity
import com.solution.nalsweather.remote.WeatherApi
import com.solution.nalsweather.repository.WeatherRepository
import com.solution.nalsweather.utils.API_LOCATION
import com.solution.nalsweather.view.calendar.CalendarAdapter
import com.solution.nalsweather.view.calendar.CalendarUtils
import com.solution.nalsweather.view.calendar.CalendarUtils.Companion.daysInWeekArray
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.abs
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity() : AppCompatActivity(), GestureDetector.OnGestureListener {
    lateinit var gestureDetector: GestureDetector
    var x2: Float = 0.0f
    var x1: Float = 0.0f
    var y1: Float = 0.0f
    var y2: Float = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Companion.binding = ActivityMainBinding.inflate(layoutInflater)
        context = this.applicationContext
        setContentView(Companion.binding.root)
        gestureDetector = GestureDetector(this, this)
        weatherStateName.put("sn", "Snow")
        weatherStateName.put("sl", "Sleet")
        weatherStateName.put("h", "Hail")
        weatherStateName.put("t", "Thunderstorm")
        weatherStateName.put("hr", "Heavy Rain")
        weatherStateName.put("lr", "Light Rain")
        weatherStateName.put("s", "Showers")
        weatherStateName.put("hc", "Heavy Cloud")
        weatherStateName.put("lc", "Light Cloud")
        weatherStateName.put("c", "Clear")
        val today = LocalDate.now()
        getValueDisplayUI(today)
        initRefreshListener()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        gestureDetector.onTouchEvent(event)
        when (event?.action) {
            //When we start swipe
            0 -> {
                x1 = event.x
                y1 = event.y
            }
            //When we end the swipe
            1 -> {
                x2 = event.x
                y2 = event.y
                val valueX: Float = x2 - x1
                val valueY: Float = y2 - y1
                if (abs(valueX) > SWIPE_MIN_DISTANCE) {
                    if (x2 > x1 && swipeWeek) {
                        indexWeek++
                        previousWeekAction()
                        if (indexWeek == 2) {
                            swipeWeek = false
                            indexWeek = 0
                        }
                    } else {
                        indexWeek++
                        nextWeekAction()
                        if (indexWeek == 2) {
                            swipeWeek = true
                            indexWeek = 0
                        }
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }

    fun previousWeekAction() {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1)
        setWeekView()
    }

    fun nextWeekAction() {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1)
        setWeekView()
    }

    object onItemListener : CalendarAdapter.OnItemListener {
        override fun onItemClick(position: Int, date: LocalDate?) {
            getValueDisplayUI(date)
        }
    }

    companion object {
        const val SWIPE_MIN_DISTANCE = 150
        lateinit var context: Context
        lateinit var binding: ActivityMainBinding
        val weatherStateAbbr: HashMap<String, Int> = HashMap<String, Int>()
        val weatherStateName: HashMap<String, String> = HashMap<String, String>()
        private var swipeWeek = false
        private var indexWeek = 1
        private var weatherState = ""
        private val humidityHandler = Handler()
        private val predictabilityHandler = Handler()
        private var humidity = 0
        private var predictability = 0
        private var thetemp = 0F
        private var displayDate = ""

        fun setWeekView() {
            val days: ArrayList<LocalDate> = daysInWeekArray(CalendarUtils.selectedDate)
            val calendarAdapter = CalendarAdapter(days, onItemListener)
            val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, 7)
            binding.calendarRecyclerView.setLayoutManager(layoutManager)
            binding.calendarRecyclerView.setAdapter(calendarAdapter)
        }

        fun updateUI() {
            var i = 0
            Log.d("weatherStateName", weatherStateName.get(weatherState).toString())
            Glide.with(context)
                .load(
                    context.getResources().getIdentifier(
                        "icon_weather_state_" + weatherState,
                        "drawable",
                        context.getPackageName()
                    )
                )
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .error(R.color.design_default_color_error)
                .into(binding.imageWeatherState)
            binding.weatherStateName.text = weatherStateName.get(weatherState)
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
            val formatter =
                DateTimeFormatter.ofPattern("EEE MMM dd, yyyy").withLocale(Locale("en", "NZ"))
            val formatterMonth = DateTimeFormatter.ofPattern("MM")
            val formatterDay = DateTimeFormatter.ofPattern("dd")
            displayDate = date!!.format(formatter)
            humidity = 0
            predictability = 0
            val retrofitService = WeatherApi.getInstance()
            var dbHelper = DatabaseHelperImpl(DatabaseBuilder.getInstance(context))
            GlobalScope.launch {
                val handlerVisiblePro = Handler(Looper.getMainLooper())
                handlerVisiblePro.post {
                    binding.progressDialog.visibility = View.VISIBLE
                }
                val mainRepository = WeatherRepository(retrofitService).getAllWeathers(
                    location = API_LOCATION,
                    year = date!!.year.toString(),
                    month = date!!.format(formatterMonth),
                    day = date!!.format(formatterDay)
                )
                var data = WeatherEntity(0, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "")
                mainRepository.body()?.forEach {

                    data.id_weather = it.id
                    data.weather_state_name = it.weather_state_name
                    data.weather_state_abbr = it.weather_state_abbr
                    data.wind_direction_compass = it.wind_direction_compass
                    data.created = it.created
                    data.applicable_date = it.applicable_date
                    data.min_temp = it.min_temp
                    data.max_temp = it.max_temp
                    data.the_temp = it.the_temp
                    data.wind_speed = it.wind_speed
                    data.air_pressure = it.air_pressure
                    data.humidity = it.humidity
                    data.visibility = it.visibility
                    data.predictability = it.predictability

                    dbHelper.insertItem(data)
                }
                var size = mainRepository.body()?.size
                if (size ?: 0 > 0) {
                    var i = 0
                    var humidityTemp = 0
                    var predictabilityTemp = 0
                    var thetempTemp = 0F
                    for (index in mainRepository.body()!!) {
                        var count = weatherStateAbbr.get(index.weather_state_abbr)
                        if (count != null) {
                            weatherStateAbbr.put(index.weather_state_abbr, count + 1)
                        } else {
                            weatherStateAbbr.put(index.weather_state_abbr, 1)
                        }
                        humidityTemp += index.humidity.toInt()
                        predictabilityTemp += index.predictability.toInt()
                        thetempTemp += index.the_temp.toFloat()
                        i++;
                    }
                    thetemp = thetempTemp / size!!
                    humidity = humidityTemp / size!!
                    predictability = predictabilityTemp / size!!
                }
                var weatherStateTemp = 0
                weatherState = ""
                for (key in weatherStateAbbr.keys) {
                    if (weatherStateAbbr[key]!! > weatherStateTemp) {
                        weatherStateTemp = weatherStateAbbr[key]!!
                        weatherState = key
                    }
                }
                val handlerInvisiblePro = Handler(Looper.getMainLooper())
                handlerInvisiblePro.post {
                    binding.progressDialog.visibility = View.INVISIBLE
                }
                val handler = Handler(Looper.getMainLooper())
                handler.post {
                    updateUI()
                }
            }
            CalendarUtils.selectedDate = date!!
            setWeekView()
        }

        fun initRefreshListener() {
            binding.swiperefresh.setOnRefreshListener {
                getValueDisplayUI(CalendarUtils.selectedDate)
                binding.swiperefresh.isRefreshing = false
            }
        }
    }

    override fun onDown(p0: MotionEvent?): Boolean {
        return false
    }

    override fun onShowPress(p0: MotionEvent?) {
    }

    override fun onSingleTapUp(p0: MotionEvent?): Boolean {
        return false
    }

    override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        return false
    }

    override fun onLongPress(p0: MotionEvent?) {
    }

    override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        return false
    }
}