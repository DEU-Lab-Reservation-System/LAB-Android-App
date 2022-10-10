package com.example.lab.data.requestDto

import com.example.lab.data.enum.Role
import com.google.gson.annotations.SerializedName

class MemberRequestDto(){
    data class Login(
        @SerializedName("userId") val userId: String,
        @SerializedName("password") val password: String,
        @SerializedName("deviceToken") val deviceToken: String
    )

    data class SignUp(
        @SerializedName("userId") val userId: String,
        @SerializedName("password") val password: String,
        @SerializedName("name") val name: String,
        @SerializedName("email") val email: String,
        @SerializedName("phoneNum") val phoneNumber: String,
        @SerializedName("role") val role: Role
    )

    data class Check(
        @SerializedName("userId") val userId: String
    )

}
