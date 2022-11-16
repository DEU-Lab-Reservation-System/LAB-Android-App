package com.example.lab.data.responseDto

import com.google.gson.annotations.SerializedName
import org.json.JSONArray
import org.json.JSONObject

class ReservResponseDto {
    data class ReservList(
        @SerializedName("reservations") val reservList:ArrayList<Reserv>
    )

    data class Reserv(
        @SerializedName("id")               val id:Int,
        @SerializedName("userId")           val userId:String,
        @SerializedName("name")             val name:String,
        @SerializedName("major")            val major:String,
        @SerializedName("roomNumber")       val roomNumber:String,
        @SerializedName("seatNum")          val seatNum:String,
        @SerializedName("startTime")        val startTime:String,
        @SerializedName("endTime")          val endTime:String,
        @SerializedName("extensionTime")    val extendableTime:String,
        @SerializedName("permission")       val permission:Boolean
    ){
        fun toJson(): JSONObject {
            return JSONObject().apply {
                put("id", id)
                put("userId", userId)
                put("name", name)
                put("major", major)
                put("roomNumber", roomNumber)
                put("seatNum", seatNum)
                put("startTime", startTime)
                put("endTime", endTime)
                put("extensionTime", extendableTime)
                put("permission", permission)
            }
        }

        companion object {
            /**
             * JSONObject를 Dto로 변환해주는 메소드
             */
            fun createDto(json:JSONObject):Reserv{
                return Reserv(
                    id = json.getInt("id"),
                    userId = json.getString("userId"),
                    name = json.getString("name"),
                    major = json.getString("major"),
                    roomNumber = json.getString("roomNumber"),
                    seatNum = json.getString("seatNum"),
                    startTime = json.getString("startTime"),
                    endTime = json.getString("endTime"),
                    extendableTime = json.getString("extensionTime"),
                    permission = json.getBoolean("permission")
                )
            }
        }
    }

    data class LabUserList(
        @SerializedName("manager")      val manager     :MemberResponseDto.Member,
        @SerializedName("reservations") val reservList  :ArrayList<Reserv>,
    )
}