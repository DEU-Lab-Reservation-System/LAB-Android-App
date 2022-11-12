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

    class ReservIdList(
        var idList:ArrayList<Int>
    )
}