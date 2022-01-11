package com.solution.nalsweather.view.calendar

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class CalendarUtils {

    fun formattedDate(date: LocalDate): String? {
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
        return date.format(formatter)
    }

    fun formattedTime(time: LocalTime): String? {
        val formatter = DateTimeFormatter.ofPattern("hh:mm:ss a")
        return time.format(formatter)
    }

    fun monthYearFromDate(date: LocalDate): String? {
        val formatter = DateTimeFormatter.ofPattern("MM")
        return date.format(formatter)
    }

    companion object {
        var selectedDate: LocalDate = LocalDate.now()
        fun daysInWeekArray(
            selectedDate: LocalDate): ArrayList<LocalDate> {
            val days = ArrayList<LocalDate>()
            var current = sundayForDate(selectedDate)
            val endDate = current!!.plusWeeks(1)
            while (current!!.isBefore(endDate)) {
                days.add(current)
                current = current.plusDays(1)
            }
            return days
        }

        fun sundayForDate(current: LocalDate): LocalDate? {
            var current = current
            val oneWeekAgo = current.minusWeeks(1)
            val formatter = DateTimeFormatter.ofPattern("EEE").withLocale(Locale("en", "NZ"))
            val today = LocalDate.now()
            val day = today.format(formatter)
            while (current.isAfter(oneWeekAgo)) {
                when (day) {
                    "Mon" -> if (current.dayOfWeek == DayOfWeek.MONDAY) return current
                    "Tue" -> if (current.dayOfWeek == DayOfWeek.TUESDAY) return current
                    "Wed" -> if (current.dayOfWeek == DayOfWeek.WEDNESDAY) return current
                    "Thu" -> if (current.dayOfWeek == DayOfWeek.THURSDAY) return current
                    "Fri" -> if (current.dayOfWeek == DayOfWeek.FRIDAY) return current
                    "Sat" -> if (current.dayOfWeek == DayOfWeek.SATURDAY) return current
                    "Sun" -> if (current.dayOfWeek == DayOfWeek.SUNDAY) return current
                    else -> if (current.dayOfWeek == DayOfWeek.MONDAY) return current
                }
                current = current.minusDays(1)
            }
            return null
        }
    }
}