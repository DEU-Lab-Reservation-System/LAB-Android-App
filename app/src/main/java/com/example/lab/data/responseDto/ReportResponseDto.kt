package com.example.lab.data.responseDto

import com.google.gson.annotations.SerializedName

class ReportResponseDto {
    data class Reports(
        @SerializedName("reports")  val reportList:ArrayList<Report>
    )
    data class Report(
        @SerializedName("id")           val id          :Int,
        @SerializedName("userId")       val userId      :String,
        @SerializedName("writerName")   val writerName  :String,
        @SerializedName("title")        val title       :String,
        @SerializedName("content")      val content     :String,
        @SerializedName("createDate")   val date        :String
    )

}