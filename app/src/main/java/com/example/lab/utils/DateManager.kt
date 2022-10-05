package com.example.lab.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.lab.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
object DateManager {
    private val daylist = arrayOf("월", "화", "수", "목", "금")

    fun day(dayCode:Int):String{
        return daylist[dayCode]
    }

    /**
     * "yyyy-MM-dd'T'HH:mm:ss" 형식의 날짜 데이터를 String으로 변환해주는 메소드
     */
    fun dateParse(date:String){
        val time:String = DateTimeFormatter.ofPattern("HH:mm").format(LocalDateTime.parse(date))
    }
}