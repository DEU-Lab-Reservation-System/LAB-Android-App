package com.example.lab.data.requestDto

import com.example.lab.data.entity.Member
import com.example.lab.data.enum.Role
import com.example.lab.data.responseDto.MemberResponseDto
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

    data class Update(
        @SerializedName("id") var id:Int,
        @SerializedName("userId") var userId:String,
        @SerializedName("password") var password:String,
        @SerializedName("name") var name:String,
        @SerializedName("major") var major:String,
        @SerializedName("phoneNum") var phoneNumber: String,
        @SerializedName("email") var email: String,
        @SerializedName("role") var role:Role,
        @SerializedName("deviceToken") var deviceToken: String,
        @SerializedName("isAuth") var isAuth:Boolean,
    ) {
        companion object{
            fun createDto(member:Member):Update{
                return member.let {
                    Update(
                        id = it.id.toInt(),
                        userId = it.userId,
                        password = it.password,
                        name = it.name,
                        major = it.major,
                        phoneNumber = it.phoneNumber,
                        email = it.email,
                        role = it.role,
                        deviceToken = it.deviceToken,
                        isAuth = it.isAuth
                    )
                }
            }

            fun createDto(member:MemberResponseDto.Member):Update{
                return member.let {
                    Update(
                        id = it.id,
                        userId = it.userId,
                        password = it.password,
                        name = it.name,
                        major = it.major,
                        phoneNumber = it.phoneNumber,
                        email = it.email,
                        role = it.role,
                        deviceToken = it.deviceToken,
                        isAuth = it.isAuth
                    )
                }
            }
        }
    }
}
