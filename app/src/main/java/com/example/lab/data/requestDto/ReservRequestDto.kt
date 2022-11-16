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

    /**
     * 승인 or 거절할 예약 리스트를 전송하는 Dto
     */
    data class Auth(
        @SerializedName("reservationIds")   var reservIdList    :ArrayList<Int>,
        @SerializedName("roomNum")          var roomNum         :String,
        @SerializedName("state")            var state           :Boolean // 승인할 정보는 True 거절할 정보는 false
    )

    /**
     * 실습실 정보를 요청으로 보낼 때 사용하는 Dto
     */
    data class LabInfo(
        @SerializedName("startTime")    val startTime: String,
        @SerializedName("endTime")      val endTime: String,
        @SerializedName("roomNum")      val roomNum:String,
    )

}