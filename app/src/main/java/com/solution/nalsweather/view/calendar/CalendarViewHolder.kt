package com.solution.nalsweather.view.calendar

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.solution.nalsweather.R
import java.time.LocalDate
import java.util.ArrayList


class CalendarViewHolder internal constructor(
    itemView: View,
    onItemListener: CalendarAdapter.OnItemListener,
    days: ArrayList<LocalDate>
) :
    RecyclerView.ViewHolder(itemView), View.OnClickListener {
    private val days: ArrayList<LocalDate>
    val parentView: View
    val dayOfMonth: TextView
    val dayOfToday: TextView
    val month: TextView
    private val onItemListener: CalendarAdapter.OnItemListener
    override fun onClick(view: View) {
        onItemListener.onItemClick(adapterPosition, days[adapterPosition])
    }

    init {
        parentView = itemView.findViewById(R.id.parentView)
        dayOfToday = itemView.findViewById(R.id.cellTodayText)
        month = itemView.findViewById(R.id.cellMonthText)
        dayOfMonth = itemView.findViewById(R.id.cellDayText)
        this.onItemListener = onItemListener
        itemView.setOnClickListener(this)
        this.days = days
    }
}