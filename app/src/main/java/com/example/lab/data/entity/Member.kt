package com.example.lab.data.entity

import com.example.lab.enum.Role
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Member {
    @Expose
    @SerializedName("id")
    var id: String? = null

    @Expose
    @SerializedName("userId")
    var userId: String? = null

    @Expose
    @SerializedName("password")
    var password: String? = null

    @Expose
    @SerializedName("name")
    var name: String? = null

    @Expose
    @SerializedName("email")
    var email: String? = null

    @Expose
    @SerializedName("phoneNum")
    var phoneNumber: String? = null

    @Expose
    @SerializedName("Role")
    var role: Role? = null

    @Expose
    @SerializedName("deviceToken")
    var deviceToken:String? = null

    @Expose
    @SerializedName("isAuth")
    var isAuth: Boolean = false

    override fun toString(): String {
        return "Member(id=$id, userId=$userId, password=$password, name=$name, email=$email, phoneNumber=$phoneNumber, role=$role, deviceToken=$deviceToken, isAuth=$isAuth)"
    }


}