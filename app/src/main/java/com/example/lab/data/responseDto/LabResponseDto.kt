package com.example.lab.data.responseDto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LabResponseDto {
    data class Status(
        @SerializedName("manager") val manager: MemberResponseDto.ManagerMember,
        @SerializedName("seatList") val seatList: ArrayList<String>,
        @SerializedName("inClass") val inClass: Boolean
    )
}