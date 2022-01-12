package com.solution.nalsweather.view.calendar

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.solution.nalsweather.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


internal class CalendarAdapter(
    private val days: ArrayList<LocalDate>,
    private val onItemListener: OnItemListener
) :
    RecyclerView.Adapter<CalendarViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.calendar_cell, parent, false)
        val layoutParams = view.layoutParams
        if (days.size > 15) //month view
            layoutParams.height = (parent.height * 0.166666666).toInt() else  // week view
            layoutParams.height = parent.height
        return CalendarViewHolder(view, onItemListener, days)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val date = days[position]
        val today = LocalDate.now()
        val formatterToday = DateTimeFormatter.ofPattern("yyyy-MM-dd").withLocale(Locale("en", "NZ"))
        if (date == null) {
            holder.dayOfMonth.setText("")
            holder.month.setText("")
            holder.dayOfToday.setText("")
        } else {
            holder.dayOfMonth.setText(date.dayOfMonth.toString())
            holder.month.setText(date.monthValue.toString())
            val formatter = DateTimeFormatter.ofPattern("EEE").withLocale(Locale("en", "NZ"))
            if (today.format(formatterToday).toString().equals(date.toString())) {
                holder.dayOfToday.setText("Today")
            } else {
                holder.dayOfToday.setText(date.format(formatter).toString())
            }
            if (date == CalendarUtils.selectedDate) holder.parentView.setBackgroundResource(R.drawable.bg_item_calendar)
        }
    }

    override fun getItemCount(): Int {
        return days.size
    }

    interface OnItemListener {
        fun onItemClick(position: Int, date: LocalDate?)
    }
}