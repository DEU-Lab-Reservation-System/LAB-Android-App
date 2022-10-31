package com.example.lab.data.responseDto

import com.google.gson.annotations.SerializedName

class MemberResponseDto {
    data class ManagerMember(
        @SerializedName("id") val id: Long,
        @SerializedName("name") var name: String,
        @SerializedName("userId") var userId: String,
        @SerializedName("phoneNum") var phoneNumber: String,
    )
}