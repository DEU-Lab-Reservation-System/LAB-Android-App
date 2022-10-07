package com.example.lab.custom.timetableview

import android.widget.TextView
import java.io.Serializable

class Sticker : Serializable{
    private var view: ArrayList<TextView> = ArrayList()
    private var schedules: ArrayList<Schedule> = ArrayList()

    fun addTextView(v: TextView) { view!!.add(v) }

    fun addSchedule(schedule: Schedule) { schedules!!.add(schedule) }

    fun getView(): ArrayList<TextView> { return view }

    fun getSchedules(): ArrayList<Schedule> { return schedules }
}