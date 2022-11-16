package com.example.lab.data.responseDto

import com.example.lab.data.enum.Role
import com.google.gson.annotations.SerializedName

class MemberResponseDto {
    data class ManagerMember(
        @SerializedName("id") val id: Long,
        @SerializedName("name") var name: String,
        @SerializedName("userId") var userId: String,
        @SerializedName("phoneNum") var phoneNumber: String,
    )

    data class Member(
        @SerializedName("id")           val id          : Int,
        @SerializedName("userId")       val userId      : String,
        @SerializedName("password")     val password    : String,
        @SerializedName("name")         val name        : String,
        @SerializedName("major")        val major       : String,
        @SerializedName("phoneNum")     val phoneNumber : String,
        @SerializedName("email")        val email       : String,
        @SerializedName("role")         val role        : Role,
        @SerializedName("deviceToken")  val deviceToken : String,
        @SerializedName("warningCount") val warningCnt  : Int,
        @SerializedName("isAuth")       val isAuth      : Boolean,
    )

    data class Members(
        @SerializedName("members") val memberList:ArrayList<MemberInfo>
    ){
        class MemberInfo(
            @SerializedName("id")       val id:Int,
            @SerializedName("name")     val name:String,
            @SerializedName("phoneNum") val phoneNumber: String,
            @SerializedName("userId")   val userId: String,
        )
    }

}