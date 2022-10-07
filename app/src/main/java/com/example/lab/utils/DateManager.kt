package com.example.lab.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
object DateManager {
    private val daylist = arrayOf("월", "화", "수", "목", "금", "토", "일")

    fun day(dayCode:Int):String{
        return daylist[dayCode]
    }

    fun day(day:String):Int{
        return when(day){
            "월요일"-> 0
            "화요일"-> 1
            "수요일"-> 2
            "목요일"-> 3
            "금요일"-> 4
            "토요일"-> 5
            "일요일"-> 6
            else -> -1
        }
    }
    /**
     * "yyyy-MM-dd'T'HH:mm:ss" 형식의 날짜 데이터를 String으로 변환해주는 메소드
     */
    fun dateParse(date:String){
        val time:String = DateTimeFormatter.ofPattern("HH:mm").format(LocalDateTime.parse(date))
    }
}