package com.example.lab.utils

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
object DateManager {
    @SuppressLint("SimpleDateFormat")
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    @SuppressLint("SimpleDateFormat")
    private val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH : mm")
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
     * "yyyy-MM-dd'T'HH:mm:ss" 형식의 날짜 데이터를 HH:mm 형식으로 변환하는 메소드
     */
    fun dateParse(date:String):String{
        return DateTimeFormatter.ofPattern("HH:mm").format(LocalDateTime.parse(date))
    }

    /**
     * yyyy-MM-dd
     * 년도부터 일 수까지 날짜로 파싱
     */
    fun getDateUntilDate(date: String):String{
        return dateFormat.format(date)
    }

    fun getDateUntilDate(date: Long):String{
        return dateFormat.format(date)
    }

    /**
     * yyyy-MM-dd HH:mm
     * 년도부터 분까지 날짜로 파싱
     */
    fun getDateUntilMinute(date: String): String{
        return dateFormat.format(date)
    }
    fun getDateUntilMinute(date: Long): String{
        return dateFormat.format(date)
    }
}