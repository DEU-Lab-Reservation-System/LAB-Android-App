package com.example.lab.remote.service

import android.os.Message
import com.example.lab.data.requestDto.MemberRequestDto
import retrofit2.Call
import com.example.lab.data.entity.Member
import com.example.lab.data.responseDto.MemberResponseDto
import com.example.lab.data.responseDto.MessageDto
import retrofit2.http.*

interface MemberService {
    @POST("api/member/login")
    fun login(@Body loginDto: MemberRequestDto.Login): Call<Member>?

    @POST("api/member")
    fun signUp(@Body signUpDto: MemberRequestDto.SignUp) : Call<MessageDto>

    @POST("api/member/check")
    fun idCheck(@Body checkDto: MemberRequestDto.Check): Call<MessageDto>

    @PUT("api/member")
    fun updateMember(@Body updateDto:MemberRequestDto.Update): Call<MemberResponseDto.Member>

    /**
     * 전체 사용자의 정보를 가져오는 메소드 (USER만)
     */
    @GET("api/member")
    fun getAllMembers():Call<MemberResponseDto.Members>

    /**
     * 회원 탈퇴
     */
    @DELETE("api/member/{userId}")
    fun withdrawal(@Path("userId") userId:String):Call<MessageDto>
}