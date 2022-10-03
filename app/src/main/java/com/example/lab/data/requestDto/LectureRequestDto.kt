package com.example.lab.data.requestDto

import com.google.gson.annotations.SerializedName

class LectrueRequestDto {

    data class Lecture(
        @SerializedName("code")         var code:String,
        @SerializedName("title")        var title:String,
        @SerializedName("professor")    var professor:String,
        @SerializedName("day")          var day:String,
        @SerializedName("roomNumber")   var roomNumber: String,
        @SerializedName("startDate")    var startDate:String,
        @SerializedName("endDate")      var endDate:String,
        @SerializedName("startTime")    var startTime:String,
        @SerializedName("endTime")      var endTime:String,
    )

}