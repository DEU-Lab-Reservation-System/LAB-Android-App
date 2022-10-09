package com.example.lab.data.responseDto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LectureResponseDto {
    data class Delete(
        @SerializedName("message") var message: String
    )
}