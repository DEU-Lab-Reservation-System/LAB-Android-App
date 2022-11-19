package com.example.lab.data.responseDto

import com.google.gson.annotations.SerializedName
import org.json.JSONObject

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
    ){
        fun toJson(): JSONObject {
            return JSONObject().apply {
                put("id", id)
                put("userId", userId)
                put("writerName", writerName)
                put("title", title)
                put("content", content)
                put("createDate", date)
            }
        }

        companion object {
            fun parseJson(json:JSONObject): Report{
                return json.let {
                    Report(
                        id = it.getInt("id"),
                        userId = it.getString("userId"),
                        writerName = it.getString("writerName"),
                        title = it.getString("title"),
                        content = it.getString("content"),
                        date = it.getString("createDate")
                    )
                }
            }
        }
    }

}