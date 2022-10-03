package com.example.lab.data.responseDto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName




class TokenResponseDto {
    data class Check(
        @Expose
        @SerializedName("auth")
        var auth:Boolean
    )
}