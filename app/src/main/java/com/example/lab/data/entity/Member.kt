package com.example.lab.data.entity

import com.example.lab.data.enum.Role
import com.example.lab.data.responseDto.MemberResponseDto
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.json.JSONObject


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
    var userId: String,

    /**
     * 사용자 비밀번호
     */
    @Expose
    @SerializedName("password")
    var password: String,

    /**
     * 사용자 이름
     */
    @Expose
    @SerializedName("name")
    var name: String,

    @Expose
    @SerializedName("major")
    var major: String,

    /**
     * 사용자 이메일
     */
    @Expose
    @SerializedName("email")
    var email: String,

    /**
     * 사용자 전화번호
     */
    @Expose
    @SerializedName("phoneNum")
    var phoneNumber: String,

    /**
     * 사용자 권한(재학생, 휴학생, 졸업생, 관리자)
     */
    @Expose
    @SerializedName("role")
    var role: Role,

    /**
     * 디바이스 토큰
     */
    @Expose
    @SerializedName("deviceToken")
    var deviceToken: String,

    /**
     * 회원 인증 여부
     */
    @Expose
    @SerializedName("isAuth")
    var isAuth: Boolean = false,

    @Expose
    @SerializedName("warningCount")
    var warningCnt: Int = 0,

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

    fun parseJson(json: JSONObject): Member {
        return json.let {
            Member(
                id = it.getLong("id"),
                userId = it.getString("userId"),
                password = it.getString("password"),
                name = it.getString("name"),
                major = it.getString("major"),
                email = it.getString("email"),
                phoneNumber = it.getString("phoneNum"),
                role = Role.valueOf(it.getString("role")),
                deviceToken = it.getString("deviceToken"),
                isAuth = it.getBoolean("isAuth"),
                warningCnt = it.getInt("warningCnt"),
            )
        }
    }
}