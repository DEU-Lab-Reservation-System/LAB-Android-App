package com.example.lab.repository

import android.util.Log
import com.example.lab.data.dto.MemberLoginDto
import com.example.lab.data.entity.Member
import com.example.lab.data.remote.RetrofitClient
import com.example.lab.data.remote.service.MemberService
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

object MemberRepository {
    private val memberService: MemberService = RetrofitClient.retrofit.create(MemberService::class.java)

    fun login(id: String, password: String): Response<Member>?{
        val memberCall: Call<Member>? = memberService.login(MemberLoginDto(id, password))

        return try {
            memberCall?.execute()
        } catch (e: IOException){
            e.printStackTrace()
            null
        }
    }
}