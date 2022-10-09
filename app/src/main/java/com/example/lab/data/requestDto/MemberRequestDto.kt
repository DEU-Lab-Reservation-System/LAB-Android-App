package com.example.lab.data.requestDto

import com.google.gson.annotations.SerializedName

class MemberRequestDto(){
    data class Login(
        @SerializedName("userId") val userId: String,
        @SerializedName("password") val password: String,
        @SerializedName("deviceToken") val deviceToken: String
    )

    data class Check(
        @SerializedName("userId") val userId: String
    )

}
