package com.example.lab.data.requestDto

import com.google.gson.annotations.SerializedName

class TokenRequestDto{
    data class Check(
        @SerializedName("userId") val userId:String,
        @SerializedName("token")  val inputToken:String
    )

    data class Create(
        @SerializedName("expireDate") val expireDate: String
    )
}