package com.example.lab.custom.timetableview

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.lab.data.entity.Lecture
import com.example.lab.utils.DateManager
import com.github.tlaabs.timetableview.Time
import org.json.JSONArray
import org.json.JSONObject
import java.io.Serializable

data class Schedule(
    var code: String? = "",
    var classTitle: String = "",
    var classPlace: String = "",
    var professorName: String = "",
    var day: Int = 0,
    var startDate:String = "",
    var endDate: String = "",
    var startTime: Time = Time(),
    var endTime: Time = Time(),
) : Serializable {

    companion object{
        const val MON = 0
        const val TUE = 1
        const val WED = 2
        const val THU = 3
        const val FRI = 4
        const val SAT = 5
        const val SUN = 6

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
                this.startDate = lecture.startDate
                this.endDate = lecture.endDate
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
                put("startDate", schedule[0].startDate)
                put("endDate",  schedule[0].endDate)

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
                            startDate = json.getString("startDate")
                            endDate = json.getString("endDate")

                            classPlace = getString("place").split(" ")?.let { it[it.lastIndex] }.toString()
                            day = getInt("day")
                            startTime = Time(getInt("startHour"), getInt("startMinute"))
                            endTime = Time(getInt("endHour"), getInt("endMinute"))
                        }
                    )
                }
            }

            return schedules
        }
    }

//    override fun toString(): String {
//        return "[Schedule(code=${code}, classTitle=${classTitle}, classPlace=${classPlace}, professorName=${professorName}, day=${day}, startTime=${startTime.hour}:${startTime.minute}, endTime=${endTime.hour}:${endTime.minute}]"
//    }
}