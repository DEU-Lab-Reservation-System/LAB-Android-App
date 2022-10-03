package com.example.lab.data.remote.service

import com.example.lab.data.requestDto.MemberRequestDto
import retrofit2.Call
import com.example.lab.data.entity.Member
import retrofit2.http.Body
import retrofit2.http.POST

interface MemberService {
    @POST("api/member/login")
    fun login(@Body loginDto: MemberRequestDto.Login): Call<Member>?
}