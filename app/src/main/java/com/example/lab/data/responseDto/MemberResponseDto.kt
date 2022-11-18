package com.example.lab.data.responseDto

import com.example.lab.data.enum.Role
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

class MemberResponseDto {
    data class ManagerMember(
        @SerializedName("id")           val id          : Long,
        @SerializedName("name")         var name        : String,
        @SerializedName("userId")       var userId      : String,
        @SerializedName("phoneNum")     var phoneNumber : String,
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
        @Expose
        @SerializedName("warningCount") val warningCnt  : Int,
        @SerializedName("isAuth")       val isAuth      : Boolean,
    ){
        fun toJson(): JSONObject{
            return JSONObject().apply {
                put("id", id)
                put("userId", userId)
                put("password", password)
                put("name", name)
                put("major", major)
                put("phoneNum", phoneNumber)
                put("email", email)
                put("role", role)
                put("deviceToken", deviceToken?:"")
                put("warningCount", warningCnt)
                put("isAuth", isAuth)
            }
        }

        companion object {
            fun parseJson(json:JSONObject): Member {
                return json.let {
                    Member(
                        id = it.getInt("id"),
                        userId = it.getString("userId"),
                        password = it.getString("password"),
                        name = it.getString("name"),
                        major = it.getString("major"),
                        email = it.getString("email"),
                        phoneNumber = it.getString("phoneNum"),
                        role = Role.valueOf(it.getString("role")),
                        deviceToken = it.getString("deviceToken")?:"",
                        isAuth = it.getBoolean("isAuth"),
                        warningCnt = 0,
                    )
                }
            }
        }
    }

    data class Members(
        @SerializedName("members")      val memberList  : ArrayList<Member>
    )

}