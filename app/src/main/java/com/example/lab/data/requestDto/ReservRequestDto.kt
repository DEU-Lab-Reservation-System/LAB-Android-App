package com.example.lab.data.requestDto

import com.google.gson.annotations.SerializedName

class ReservRequestDto {
    data class Create(
        @SerializedName("userId")   var userId      :String,
        @SerializedName("roomNum")  var labNum      :String,
        @SerializedName("teamSize") var team        :Int,
        @SerializedName("seatNum")  var seatNum     :String,
        @SerializedName("startTime")var startTime   :String, // HH:mm
        @SerializedName("endTime")  var endTime     :String, // HH:mm
    )

}