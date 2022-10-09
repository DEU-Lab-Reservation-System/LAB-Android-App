package com.example.lab.data.remote.service

import com.example.lab.data.requestDto.MemberRequestDto
import retrofit2.Call
import com.example.lab.data.entity.Member
import com.example.lab.data.responseDto.MemberResponseDto
import com.example.lab.data.responseDto.MessageDto
import retrofit2.http.Body
import retrofit2.http.POST

interface MemberService {
    @POST("api/member/login")
    fun login(@Body loginDto: MemberRequestDto.Login): Call<Member>?

    @POST("api/member/check")
    fun idCheck(@Body checkDto: MemberRequestDto.Check): Call<MessageDto>
}