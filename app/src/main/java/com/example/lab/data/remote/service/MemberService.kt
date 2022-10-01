package com.example.lab.data.remote.service

import com.example.lab.data.dto.MemberLoginDto
import retrofit2.Call
import com.example.lab.data.entity.Member
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.POST
import retrofit2.http.Query

interface MemberService {
    @POST("api/member/login")
    fun login(@Body loginDto: MemberLoginDto): Call<Member>?
}