package com.example.lab.data.requestDto

import com.google.gson.annotations.SerializedName

class MemberRequestDto(){
    data class Login(
        @SerializedName("userId")       var userId:String,
        @SerializedName("password")     var password: String,
        @SerializedName("deviceToken")  var deviceToken:String
        )


}
