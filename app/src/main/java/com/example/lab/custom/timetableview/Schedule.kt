package com.example.lab.custom.timetableview

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.lab.data.entity.Lecture
import com.example.lab.utils.DateManager
import com.github.tlaabs.timetableview.Time
import retrofit2.http.Field
import java.io.Serializable

data class Schedule(var code:String?="") : com.github.tlaabs.timetableview.Schedule(), Serializable{
    companion object{
        @RequiresApi(Build.VERSION_CODES.O)
        fun createSchedule(lecture: Lecture): Schedule{
            val startTime = lecture.startTime!!.split(":")
            val endTime = lecture.endTime!!.split(":")

            return Schedule().apply {
                this.code = lecture.code
                this.classTitle = lecture.title
                this.classPlace = "정보공학관 ${lecture.place}"
                this.professorName = lecture.professor
                this.day = DateManager.day(lecture.day!!)
                this.startTime = Time(startTime[0].toInt(), startTime[1].toInt())
                this.endTime = Time(endTime[0].toInt(), endTime[1].toInt())
            }
        }
    }

    override fun toString(): String {
        return "[Schedule(code=${code}, classTitle=${classTitle}, classPlace=${classPlace}, professorName=${professorName}, day=${day}, startTime=${startTime.hour}:${startTime.minute}, endTime=${endTime.hour}:${endTime.minute}]"
    }
}