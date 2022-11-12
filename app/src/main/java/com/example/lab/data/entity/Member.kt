package com.example.lab.data.entity

import com.example.lab.data.enum.Role
import com.example.lab.data.responseDto.MemberResponseDto
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class Member(
    /**
     * id (AutoIncrement)
     */
    @Expose
    @SerializedName("id")
    var id: Long = 0,

    /**
     *  사용자 id
     */
    @Expose
    @SerializedName("userId")
    var userId: String? = null,

    /**
     * 사용자 비밀번호
     */
    @Expose
    @SerializedName("password")
    var password: String? = null,

    /**
     * 사용자 이름
     */
    @Expose
    @SerializedName("name")
    var name: String? = null,

    @Expose
    @SerializedName("major")
    var major: String? = null,

    /**
     * 사용자 이메일
     */
    @Expose
    @SerializedName("email")
    var email: String? = null,

    /**
     * 사용자 전화번호
     */
    @Expose
    @SerializedName("phoneNum")
    var phoneNumber: String? = null,

    /**
     * 사용자 권한(재학생, 휴학생, 졸업생, 관리자)
     */
    @Expose
    @SerializedName("role")
    var role: Role? = null,

    /**
     * 디바이스 토큰
     */
    @Expose
    @SerializedName("deviceToken")
    var deviceToken: String? = null,

    /**
     * 회원 인증 여부
     */
    @Expose
    @SerializedName("isAuth")
    var isAuth: Boolean = false,

    /**
     * 사용자 예약 내역
     */
    @Expose
    @SerializedName("reservation")
    var reservation: Reservation? = null,
    ) {
    override fun toString(): String {
        return "Member(id=$id, userId=$userId, password=$password, name=$name, email=$email, phoneNumber=$phoneNumber, role=$role, deviceToken=$deviceToken, isAuth=$isAuth, reservation=$reservation)"
    }

    fun update(update: MemberResponseDto.Member) {
        update.let {
            id = it.id.toLong()
            userId = it.userId
            name = it.name
            password = it.password
            major = it.major
            phoneNumber = it.phoneNumber
            email = it.email
            isAuth = it.isAuth
            role = it.role
        }

    }
}