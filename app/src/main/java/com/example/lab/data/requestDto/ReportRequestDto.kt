package com.example.lab.data.requestDto

import com.google.gson.annotations.SerializedName

class ReportRequestDto {
    data class Send(
        @SerializedName("userId")       val userId      : String,
        @SerializedName("writerName")   val userName    : String,
        @SerializedName("title")        val title       :String,
        @SerializedName("content")      val content     :String
    )
}