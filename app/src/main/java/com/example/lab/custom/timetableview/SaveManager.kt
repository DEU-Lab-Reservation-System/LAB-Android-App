package com.example.lab.custom.timetableview

import com.github.tlaabs.timetableview.Time
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.util.*

class SaveManager {
    companion object{
        fun saveSticker(stickers: HashMap<Int, Sticker>): String {
            val obj1 = JsonObject()
            val arr1 = JsonArray()
            val orders = getSortedKeySet(stickers)
            for (i in orders.indices) {
                val obj2 = JsonObject()
                val idx = orders[i]
                obj2.addProperty("idx", orders[i])
                val arr2 = JsonArray() //5
                val schedules = stickers[idx]!!.getSchedules()

                if (schedules != null) {
                    for (schedule in schedules) {
                        val obj3 = JsonObject()
                        obj3.addProperty("code", schedule.code)
                        obj3.addProperty("classTitle", schedule.classTitle)
                        obj3.addProperty("classPlace", schedule.classPlace)
                        obj3.addProperty("professorName", schedule.professorName)
                        obj3.addProperty("day", schedule.day)
                        obj3.addProperty("startDate", schedule.startDate)
                        obj3.addProperty("endDate", schedule.endDate)

                        val obj4 = JsonObject() //startTime
                        obj4.addProperty("hour", schedule.startTime.hour)
                        obj4.addProperty("minute", schedule.startTime.minute)
                        obj3.add("startTime", obj4)

                        val obj5 = JsonObject() //endtTime
                        obj5.addProperty("hour", schedule.endTime.hour)
                        obj5.addProperty("minute", schedule.endTime.minute)
                        obj3.add("endTime", obj5)

                        arr2.add(obj3)
                    }
                }
                obj2.add("schedule", arr2)
                arr1.add(obj2)
            }
            obj1.add("sticker", arr1)
            return obj1.toString()
        }

        fun loadSticker(json: String?): HashMap<Int, Sticker> {
            val stickers = HashMap<Int, Sticker>()
            val parser = JsonParser()
            val obj1 = parser.parse(json) as JsonObject
            val arr1 = obj1.getAsJsonArray("sticker")

            for (i in 0 until arr1.size()) {
                val sticker = Sticker()
                val obj2 = arr1[i] as JsonObject
                val idx = obj2["idx"].asInt
                val arr2 = obj2["schedule"] as JsonArray

                for (k in 0 until arr2.size()) {
                    val schedule = Schedule()
                    val obj3 = arr2[k] as JsonObject
                    schedule.code = obj3["code"].asString
                    schedule.classTitle = obj3["classTitle"].asString
                    schedule.classPlace = obj3["classPlace"].asString
                    schedule.professorName = obj3["professorName"].asString
                    schedule.day = obj3["day"].asInt
                    schedule.startDate = obj3["startDate"].asString
                    schedule.endDate = obj3["endDate"].asString

                    val startTime = Time()
                    val obj4 = obj3["startTime"] as JsonObject
                    startTime.hour = obj4["hour"].asInt
                    startTime.minute = obj4["minute"].asInt

                    val endTime = Time()
                    val obj5 = obj3["endTime"] as JsonObject
                    endTime.hour = obj5["hour"].asInt
                    endTime.minute = obj5["minute"].asInt

                    schedule.startTime = startTime
                    schedule.endTime = endTime

                    sticker.addSchedule(schedule)
                }
                stickers[idx] = sticker
            }
            return stickers
        }


        private fun getSortedKeySet(stickers: HashMap<Int, Sticker>): IntArray {
            val orders = IntArray(stickers.size)
            var i = 0
            for (key in stickers.keys) {
                orders[i++] = key
            }
            Arrays.sort(orders)
            return orders
        }
    }

}