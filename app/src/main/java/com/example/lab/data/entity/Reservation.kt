package com.example.lab.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime


class Reservation {
    /**
     * primary key로 활용되는 id
     */
    @Expose
    @SerializedName("id")
    val id: Long? = null

    /**
     * 실습실 호수
     */
    @Expose
    @SerializedName("roomNumber")
    val roomNumber: String? = null

    /**
     * 예약한 좌석
     */
    @Expose
    @SerializedName("seatNum")
    val seatNum: String? = null

    /**
     * 이용 시작 시간
     */
    @Expose
    @SerializedName("startTime")
    val startTime: String? = null

    /**
     * 이용 종료 시간(만료시간)
     */
    @Expose
    @SerializedName("endTime")
    val endTime: String? = null

    /**
     * 연장 가능한 시간
     */
    @Expose
    @SerializedName("extenstionTime")
    val extensionTime: String? = null

    /**
     * 예약 승인 여부(조교로부터)
     */
    @Expose
    @SerializedName("permission")
    val permission: Boolean? = null

    override fun toString(): String {
        return "Reservation(id=$id, roomNumber=$roomNumber, seatNum=$seatNum, startTime=$startTime, endTime=$endTime, extensionTime=$extensionTime, permission=$permission)"
    }


}