package com.example.lab.data.responseDto

import com.google.gson.annotations.SerializedName

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
    )
}