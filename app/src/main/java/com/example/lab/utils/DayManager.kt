package com.example.lab.utils

import com.example.lab.R

object DayManager {
    private val daylist = arrayOf("월", "화", "수", "목", "금")

    fun day(dayCode:Int):String{
        return daylist[dayCode]
    }
}