package com.example.lab.data.requestDto

import com.google.gson.annotations.SerializedName

class LabRequestDto {
    data class TimeRange(
        @SerializedName("startTime")    val startTime :String,
        @SerializedName("endTime")      val endTime   :String,
    )
}