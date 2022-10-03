package com.example.lab.data.requestDto

import com.example.lab.data.entity.Lecture
import com.google.gson.annotations.SerializedName

class LectureRequestDto {

    data class Create(
        @SerializedName("code")         var code:String,
        @SerializedName("title")        var title:String,
        @SerializedName("professor")    var professor:String,
        @SerializedName("day")          var day:String,
        @SerializedName("roomNumber")   var roomNumber: String,
        @SerializedName("startDate")    var startDate:String,
        @SerializedName("endDate")      var endDate:String,
        @SerializedName("startTime")    var startTime:String,
        @SerializedName("endTime")      var endTime:String,
    ){
        companion object{
            fun createDto(lecture: Lecture): Create{
                return Create(
                    code = lecture.code!!,
                    title = lecture.title!!,
                    professor = lecture.professor!!,
                    day = lecture.day!!,
                    roomNumber = lecture.place!!.split(" ")[1],
                    startDate = lecture.startDate!!,
                    endDate = lecture.endDate!!,
                    startTime = lecture.startTime!!,
                    endTime = lecture.endTime!!
                )
            }
        }
    }

}