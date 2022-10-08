package com.example.lab.custom.timetableview

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.lab.data.entity.Lecture
import com.example.lab.utils.DateManager
import com.github.tlaabs.timetableview.Time
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.http.Field
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

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

        /**
         * @param schedule: ArrayList<Schedule>
         * Schedule 리스트를 JSON으로 변환하는 메소드
         */
        fun toJson(schedule:ArrayList<Schedule>):JSONObject {
            var classInfo = JSONObject()
            classInfo.apply {
                put("code", schedule[0].code)
                put("classTitle", schedule[0].classTitle)
                put("professor", schedule[0].professorName)

                var classSubInfo = JSONArray()
                schedule.forEach{
                    val info = JSONObject()
                    info.put("day", it.day)
                    info.put("place", it.classPlace)
                    info.put("startHour", it.startTime.hour)
                    info.put("startMinute", it.startTime.minute)
                    info.put("endHour", it.endTime.hour)
                    info.put("endMinute", it.endTime.minute)

                    classSubInfo.put(info)
                }

                put("subInfo", classSubInfo)
            }

            return classInfo
        }

        /**
         * @param json: JSONObject
         * JSON 데이터를 Schedule 리스트로 변환하는 메소드
         */
        fun toScheduleList(json:JSONObject): ArrayList<Schedule> {
            val schedules = ArrayList<Schedule>()

            val subInfo = json.getJSONArray("subInfo")

            for(i in 0 until subInfo.length()){
                (subInfo[i] as JSONObject).apply {
                    schedules.add(
                        Schedule().apply {
                            code = json.getString("code")
                            classTitle = json.getString("classTitle")
                            professorName = json.getString("professor")
                            day = getInt("day")
                            classPlace = getString("place").split(" ")?.let { it[it.lastIndex] }
                            startTime = Time(getInt("startHour"), getInt("startMinute"))
                            endTime = Time(getInt("endHour"), getInt("endMinute"))
                        }
                    )
                }
            }

            return schedules
        }
    }

    override fun toString(): String {
        return "[Schedule(code=${code}, classTitle=${classTitle}, classPlace=${classPlace}, professorName=${professorName}, day=${day}, startTime=${startTime.hour}:${startTime.minute}, endTime=${endTime.hour}:${endTime.minute}]"
    }
}